; RepastInstallerScript.nsi
;
; This is an installer script for Repast Simphony
;
;--------------------------------

; user the modern UI
!include "MUI.nsh"

!define VERSION "2.1-beta-2"

; The name of the installer
Name "Repast Simphony ${VERSION}"

; The file to write
OutFile "Repast-Simphony-${VERSION}-win.exe"

; The default installation directory
;InstallDir $PROGRAMFILES\RepastSimphony-${VERSION}
; changed to avoid user permissions problems with $PROGRAMFILES
InstallDir C:\RepastSimphony-${VERSION}


; Request Administrator level application privileges when copying files
RequestExecutionLevel admin

;--------------------------------

; Pages

!define MUI_PAGE_CUSTOMFUNCTION_PRE WelcomePageSetupLinkPre
!define MUI_PAGE_CUSTOMFUNCTION_SHOW WelcomePageSetupLinkShow
!define MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE repast-license.txt
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------

; The stuff to install
Section "Repast Simphony"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Store the files.
  File /r "*.*"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM Software\RepastSimphony-${VERSION} "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}" "DisplayName" "Repast Simphony"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"
 
  ; Install for all users
  SetShellVarContext all
  
  CreateDirectory "$SMPROGRAMS\RepastSimphony-${VERSION}"
  CreateDirectory "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation"

  
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Simphony.lnk" "$INSTDIR\eclipse\eclipse.exe" "" "$INSTDIR\eclipse\eclipse.exe" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Batch Runner.lnk" "$INSTDIR\batch_runner.jar" "" "$INSTDIR\batch_runner.jar"
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\ReLogo Getting Started.lnk" "$INSTDIR\docs\ReLogoGettingStarted.pdf" "" "$INSTDIR\docs\ReLogoGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Java Getting Started.lnk" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" "" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Flow Getting Started.lnk" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" "" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Reference.lnk" "$INSTDIR\docs\RepastReference.pdf" "" "$INSTDIR\docs\RepastReference.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony API.lnk" "$INSTDIR\docs\RepastSimphonyAPI\index.html" "" "$INSTDIR\docs\RepastSimphonyAPI\index.html" 0

  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\ReLogo Primitives Quick Reference.lnk" "$INSTDIR\docs\RepastSimphonyAPI\?.html" "" "$INSTDIR\docs\RepastSimphonyAPI\?.html" 0

  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony Statecharts Guide.lnk" "$INSTDIR\docs\Statecharts.pdf" "" "$INSTDIR\docs\Statecharts.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony System Dynamics Getting Started.lnk" "$INSTDIR\docs\RepastSystemDynamicsGettingStarted.pdf" "" "$INSTDIR\docs\RepastSystemDynamicsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony Data Collection.lnk" "$INSTDIR\docs\DataCollection.pdf" "" "$INSTDIR\docs\DataCollection.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Batch Getting Started.lnk" "$INSTDIR\docs\RepastBatchRunsGettingStarted.pdf" "" "$INSTDIR\docs\RepastBatchRunsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Model Testing Guide.lnk" "$INSTDIR\docs\RepastModelTesting.pdf" "" "$INSTDIR\docs\RepastModelTesting.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony FAQ.lnk" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" "" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Parameter Sweeps Getting Started.lnk" "$INSTDIR\docs\RepastParameterSweepsGettingStarted.pdf" "" "$INSTDIR\docs\RepastParameterSweepsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\UnInstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0  
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  ; Install for all users
  SetShellVarContext all
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}"
  DeleteRegKey HKLM SOFTWARE\RepastSimphony-${VERSION}

  ; Remove program group dir
  RMDir /r "$SMPROGRAMS\RepastSimphony-${VERSION}"
  
  ; Remove the install dir and the docs and eclipse sub-dirs so user 
  ; won't accidentally delete workspace files
  DELETE $INSTDIR\*.*
  RMDir /r $INSTDIR\docs
  RMDir /r $INSTDIR\eclipse
  RMDir /r $INSTDIR\models
  RMDir /r $INSTDIR

SectionEnd

Function WelcomePageSetupLinkPre
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Settings" "Numfields" "4" ; increase counter
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 3" "Bottom" "122" ; limit size of the upper label
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Type" "Link"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Text" "http://repast.sourceforge.net/"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "State" "http://repast.sourceforge.net/"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Left" "120"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Right" "315"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Top" "123"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Bottom" "132"
FunctionEnd
 
Function WelcomePageSetupLinkShow
  ; Thanks to pengyou
  ; Fix colors of added link control
  ; See http://forums.winamp.com/showthread.php?s=&threadid=205674
  Push $0
 
  GetDlgItem $0 $MUI_HWND 1203
  SetCtlColors $0 "0000FF" "FFFFFF"
  ; underline font
  CreateFont $1 "$(^Font)" "$(^FontSize)" "400" /UNDERLINE 
  SendMessage $0 ${WM_SETFONT} $1 1 
  Pop $0
 
FunctionEnd
 
;--------------------------------
;Languages
 
!insertmacro MUI_LANGUAGE "English"
