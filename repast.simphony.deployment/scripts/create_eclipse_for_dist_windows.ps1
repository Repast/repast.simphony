# Script used to build a new Repast Simphony Eclipse distribution for Windows (native x86_64).
#
# Run from the directory that should contain the resulting "eclipse" folder, e.g.:
#   powershell -ExecutionPolicy Bypass -File .\create_eclipse_for_dist_windows.ps1
#
# alternatively create a new relase folder and run from there, e.g.:
# powershell L:\Repast-Simphony\repast.simphony.deployment\scripts\create_eclipse_for_dist_windows.ps1
$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$VERSION = "2.12.0"
$ROOT = (Get-Location).Path

# --- Safety guards, evaluated before any destructive operation ---------------
# This script force-deletes directories under $ROOT and uses $env:USERPROFILE to
# locate inputs. Bail out early if those anchors look wrong, so a stray working
# directory or an empty USERPROFILE cannot cause deletion of the wrong tree.
if ([string]::IsNullOrWhiteSpace($env:USERPROFILE)) {
    throw "USERPROFILE is not set; refusing to run (paths would resolve to the drive root)."
}
if ($ROOT -eq $env:USERPROFILE -or $ROOT -match '^[A-Za-z]:\\?$') {
    throw "Refusing to run from '$ROOT'. Run from a dedicated build directory."
}

# Delete a directory only if it exists. Deliberately omits -ErrorAction
# SilentlyContinue so a locked/permission failure surfaces (via the global
# $ErrorActionPreference = 'Stop') instead of silently leaving a half-deleted
# tree for the following copy/unzip to merge into.
function Remove-Tree {
    param([Parameter(Mandatory)][string]$Path)
    if (Test-Path -LiteralPath $Path) {
        Remove-Item -LiteralPath $Path -Recurse -Force
    }
}

# Fast recursive copy via robocopy /MT - much faster than Copy-Item -Recurse (or
# Expand-Archive) for the thousands of small files in an Eclipse package. robocopy
# exit codes 0-7 are success (1 = files copied); >=8 is a real failure. Under
# PowerShell 7.4+ a nonzero native exit would otherwise be a terminating error, so
# suppress that for the call and check the code ourselves.
function Copy-Tree {
    param(
        [Parameter(Mandatory)][string]$Source,
        [Parameter(Mandatory)][string]$Destination
    )
    $hadPref = Test-Path variable:\PSNativeCommandUseErrorActionPreference
    if ($hadPref) {
        $saved = $PSNativeCommandUseErrorActionPreference
        $PSNativeCommandUseErrorActionPreference = $false
    }
    robocopy $Source $Destination /E /MT /NFL /NDL /NJH /NJS /NP | Out-Null
    $rc = $LASTEXITCODE
    if ($hadPref) { $PSNativeCommandUseErrorActionPreference = $saved }
    if ($rc -ge 8) {
        throw "robocopy failed (exit $rc) copying '$Source' to '$Destination'."
    }
    $global:LASTEXITCODE = 0
}

$TMP  = Join-Path $ROOT "tmp"
New-Item -ItemType Directory -Force -Path $TMP | Out-Null

# Eclipse Committers release to build from. Update this when moving to a newer
# release. Point this at an already-extracted fresh Eclipse download folder in
# Downloads (extracting the zip once with a fast tool like WinRAR is far quicker
# than PowerShell's Expand-Archive, and the copy below is cheap to repeat).
$ECLIPSE_RELEASE = "2026-03-R"
$ECLIPSE_SRC_DIR = Join-Path $env:USERPROFILE "Downloads\eclipse-committers-$ECLIPSE_RELEASE-win32-x86_64"

# The committers zip contains a top-level "eclipse" folder (with eclipse.exe /
# eclipsec.exe, the console launcher used for headless p2 director runs), so the
# install is usually <download>\eclipse. Fall back to the download folder itself
# in case it was extracted without that wrapper.
if (Test-Path -LiteralPath (Join-Path $ECLIPSE_SRC_DIR "eclipse\eclipsec.exe")) {
    $ECLIPSE_SRC = Join-Path $ECLIPSE_SRC_DIR "eclipse"
} elseif (Test-Path -LiteralPath (Join-Path $ECLIPSE_SRC_DIR "eclipsec.exe")) {
    $ECLIPSE_SRC = $ECLIPSE_SRC_DIR
} else {
    throw "No Eclipse install (eclipsec.exe) found under: $ECLIPSE_SRC_DIR"
}

# Remove any old eclipse and copy in the fresh install.
Remove-Tree (Join-Path $ROOT "eclipse")
Copy-Tree -Source $ECLIPSE_SRC -Destination (Join-Path $ROOT "eclipse")

# Repast plugin
# look at the features in a working install to get the feature names

# Unpack the local Repast update site and the xpand mirror used as p2 repositories.
$UPDATE_SITE_ZIP = Join-Path $env:USERPROFILE "Downloads\repast.simphony.updatesite.$VERSION.zip"
Remove-Tree (Join-Path $ROOT "repast.simphony.updatesite")
Expand-Archive -Path $UPDATE_SITE_ZIP -DestinationPath $ROOT -Force

# p2 repositories. Build the local file:/// URI via [System.Uri] so that spaces and
# other special characters in $ROOT (e.g. "D:\Repast Releases\...") are properly
# percent-encoded (a raw space in a URI is invalid and breaks p2 arg parsing).
$UPDATE_SITE_URI = ([System.Uri](Join-Path $ROOT "repast.simphony.updatesite")).AbsoluteUri

$REPOSITORIES = @(
    "https://download.eclipse.org/releases/2025-12",
    $UPDATE_SITE_URI
) -join ","


$SIMPHONY_FEATURES = "repast.simphony.feature.feature.group"

$WILDWEB      = "org.eclipse.wildwebdeveloper.feature.feature.group"
$WILDWEB_NODE = "org.eclipse.wildwebdeveloper.embedder.node.feature.feature.group"
$M2E = @(
    "org.eclipse.m2e.feature.feature.group",
    "org.eclipse.m2e.pde.feature.feature.group",
    "org.eclipse.m2e.lemminx.feature.feature.group"
) -join ","

$NEWS_FEED_FEATURE = "org.eclipse.recommenders.news.rcp.feature.feature.group"

# Point JAVA_HOME at a Windows Temurin 17 JDK and put it first on the PATH.
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"
$env:PATH = (Join-Path $env:JAVA_HOME "bin") + ";" + $env:PATH

$INSTALL_LOG = Join-Path $ROOT "eclipse_install.log"
Remove-Item -Force -ErrorAction SilentlyContinue $INSTALL_LOG

# Workspace used as -data for the headless p2 director runs. It is recreated fresh
# before each run (mirrors the rm -rf $WORKSPACE in the macOS script). Kept under
# the build-local $TMP - not the user profile - so it can never collide with a real
# Eclipse workspace the developer uses.
$WORKSPACE = Join-Path $TMP "p2-director-workspace"

# Destination of the install - the Eclipse folder being built.
$DESTINATION = Join-Path $ROOT "eclipse"

# The console launcher. Use eclipsec.exe so the process is headless and waits.
$ECLIPSEC = Join-Path $DESTINATION "eclipsec.exe"

# Helper to run a p2 director operation against the eclipse install and wait for it.
# $Description is a short, human-readable banner shown before the run so the console
# indicates which long-running step is in progress.
function Invoke-P2Director {
    param(
        [Parameter(Mandatory)][string]$Description,
        [Parameter(Mandatory)][string[]]$ExtraArgs
    )

    Write-Host ""
    Write-Host "==> $Description ..." -ForegroundColor Cyan

    Remove-Tree $WORKSPACE

    # -consoleLog surfaces the OSGi/p2 log output so the run shows interim activity
    # (fetching repositories, resolving, installing) instead of sitting silent for
    # minutes. -followReferences lets the local update site's p2 repository references
    # pull in the Groovy (e4.39) and Xpand-mirror repos, so they don't need to be
    # listed in $REPOSITORIES. Requires network access to those remote mirrors.
    $baseArgs = @(
        "-clean", "-purgeHistory",
        "-consoleLog",
        "-application", "org.eclipse.equinox.p2.director",
        "-repository", $REPOSITORIES,
        "-followReferences",
        "-destination", $DESTINATION,
        "-data", $WORKSPACE
    ) + $ExtraArgs

    # Invoke eclipsec.exe directly with the call operator. Unlike
    # Start-Process -ArgumentList <array>, the call operator quotes arguments that
    # contain spaces (e.g. paths under "D:\Repast Releases\..."), so -repository,
    # -destination, and -data survive intact. eclipsec.exe is a console launcher,
    # so the call blocks until it exits.
    # The native-exit preference is suppressed so we can report our own message
    # instead of PowerShell 7.4+ throwing on the nonzero exit first.
    $hadPref = Test-Path variable:\PSNativeCommandUseErrorActionPreference
    if ($hadPref) {
        $saved = $PSNativeCommandUseErrorActionPreference
        $PSNativeCommandUseErrorActionPreference = $false
    }
    # eclipsec.exe writes informational logging to stderr (e.g. Apache Aries
    # SPI-Fly). Under $ErrorActionPreference = 'Stop', PowerShell turns the first
    # native stderr line into a terminating NativeCommandError, aborting an
    # otherwise-successful run. Lower it to 'Continue' for the call; genuine failures
    # are detected from the exit code below.
    #
    # 2>&1 merges stderr into the output stream; casting each item to a string with
    # "$_" renders both stdout lines and native stderr (ErrorRecord) as plain text
    # (not red error formatting); Tee-Object shows the output live on the console and
    # appends the same lines to the log.
    $savedEAP = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    & $ECLIPSEC @baseArgs 2>&1 | ForEach-Object { "$_" } | Tee-Object -FilePath $INSTALL_LOG -Append
    $rc = $LASTEXITCODE
    $ErrorActionPreference = $savedEAP
    if ($hadPref) { $PSNativeCommandUseErrorActionPreference = $saved }

    if ($rc -ne 0) {
        throw "p2 director failed (exit $rc) for args: $($ExtraArgs -join ' '). See $INSTALL_LOG"
    }
    Write-Host "    done." -ForegroundColor Green
}

# Install Repast Simphony feature
Invoke-P2Director -Description "Installing Repast Simphony feature" -ExtraArgs @("-installIU", "$SIMPHONY_FEATURES")

# Uninstall m2e, wild web developer, and its embedded node.
Invoke-P2Director -Description "Uninstalling m2e" -ExtraArgs @("-uninstallIU", $M2E)
Invoke-P2Director -Description "Uninstalling Wild Web Developer" -ExtraArgs @("-uninstallIU", $WILDWEB)
Invoke-P2Director -Description "Uninstalling Wild Web Developer embedded Node" -ExtraArgs @("-uninstallIU", $WILDWEB_NODE)

# News feed doesn't seem to be a feature in current Eclipse
# Invoke-P2Director -ExtraArgs @("-uninstallIU", $NEWS_FEED_FEATURE)

# Remove references to the local Repast update site from the p2 profile prefs so the
# distributed Eclipse does not try to reach a path that only existed on the build machine.
$PROFILE_SETTINGS = Join-Path $DESTINATION "p2\org.eclipse.equinox.p2.engine\profileRegistry\epp.package.committers.profile\.data\.settings"
foreach ($prefName in @("org.eclipse.equinox.p2.artifact.repository.prefs",
                        "org.eclipse.equinox.p2.metadata.repository.prefs")) {
    $pref = Join-Path $PROFILE_SETTINGS $prefName
    if (Test-Path $pref) {
        (Get-Content $pref) | Where-Object { $_ -notmatch "repast.simphony.updatesite" } | Set-Content $pref
    }
}

# Drop recent workspace history.
$PREFS = Join-Path $DESTINATION "configuration\.settings\org.eclipse.ui.ide.prefs"
if (Test-Path $PREFS) {
    (Get-Content $PREFS) | Where-Object { $_ -notmatch "^RECENT_WORKSPACES=" } | Set-Content $PREFS
}

# Turn off auto updating in the packaged plugin_customization.ini. The committers
# package folder is versioned (e.g. org.eclipse.epp.package.committers_4.38.0.20251204-0849),
# so locate the file by wildcard instead of hard coding the version.
$PLUGIN_CUST_FILE = Get-ChildItem -Path (Join-Path $DESTINATION "plugins") `
    -Filter "plugin_customization.ini" -Recurse -ErrorAction SilentlyContinue |
    Where-Object { $_.DirectoryName -match "org\.eclipse\.epp\.package\." } |
    Select-Object -First 1 -ExpandProperty FullName
if ($PLUGIN_CUST_FILE -and (Test-Path $PLUGIN_CUST_FILE)) {
    (Get-Content $PLUGIN_CUST_FILE) `
        -replace "org.eclipse.equinox.p2.ui.sdk.scheduler/enabled=true",
                 "org.eclipse.equinox.p2.ui.sdk.scheduler/enabled=false" |
        Set-Content $PLUGIN_CUST_FILE
} else {
    Write-Warning "plugin_customization.ini not found under an org.eclipse.epp.package.* plugin; skipping auto-update disable."
}


Write-Host "Done building Windows Eclipse distribution in $DESTINATION"

