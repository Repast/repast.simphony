; RepastInstallerScript.nsi
;
; This is an installer script for Repast Simphony
;
;--------------------------------

!include "MUI.nsh"        ; user the modern UI
!include "LogicLib.nsh"   ; Library for logical statements 
!include "StrFunc.nsh"    ; String functions
!include "x64.nsh"        ; Macros for x64 machines

 ${StrTok} # Supportable for Install Sections and Functions

!define VERSION "2.2"

; The name of the installer
Name "Repast Simphony ${VERSION}"

; The file to write
OutFile "Repast-Simphony-${VERSION}-win32.exe"

; The default installation directory
;InstallDir $PROGRAMFILES\RepastSimphony-${VERSION}
; changed to avoid user permissions problems with $PROGRAMFILES
InstallDir C:\RepastSimphony-${VERSION}

; The required Java version to run Repast
!define JRE_VERSION "1.8"

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
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\UnInstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0  

  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\ReLogo Getting Started.lnk" "$INSTDIR\docs\ReLogoGettingStarted.pdf" "" "$INSTDIR\docs\ReLogoGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Java Getting Started.lnk" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" "" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Flow Getting Started.lnk" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" "" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Reference.lnk" "$INSTDIR\docs\RepastReference.pdf" "" "$INSTDIR\docs\RepastReference.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony API.lnk" "$INSTDIR\docs\RepastSimphonyAPI\index.html" "" "$INSTDIR\docs\RepastSimphonyAPI\index.html" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\ReLogo Primitives Quick Reference.lnk" "$INSTDIR\docs\RepastSimphonyAPI\ReLogoPrimitives.html" "" "$INSTDIR\docs\RepastSimphonyAPI\ReLogoPrimitives.html" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony Statecharts Guide.lnk" "$INSTDIR\docs\Statecharts.pdf" "" "$INSTDIR\docs\Statecharts.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony System Dynamics Getting Started.lnk" "$INSTDIR\docs\RepastSystemDynamicsGettingStarted.pdf" "" "$INSTDIR\docs\RepastSystemDynamicsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony Data Collection.lnk" "$INSTDIR\docs\DataCollection.pdf" "" "$INSTDIR\docs\DataCollection.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Batch Getting Started.lnk" "$INSTDIR\docs\RepastBatchRunsGettingStarted.pdf" "" "$INSTDIR\docs\RepastBatchRunsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Model Testing Guide.lnk" "$INSTDIR\docs\RepastModelTesting.pdf" "" "$INSTDIR\docs\RepastModelTesting.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Repast Simphony FAQ.lnk" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" "" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Documentation\Upgrading_Repast_Simphony.lnk" "$INSTDIR\docs\Upgrading_Repast_Simphony.txt" "" "$INSTDIR\docs\Upgrading_Repast_Simphony.txt" 0  
  
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
  
  ; Remove the install dir and the docs and eclipse sub-dirs 
  DELETE $INSTDIR\*.*
  RMDir /r $INSTDIR\docs
  RMDir /r $INSTDIR\eclipse
  RMDir /r $INSTDIR\models
  RMDir /r $INSTDIR

SectionEnd

; Detects if a Java installation exists in the Windows registry and compares to the required version
Function DetectJRE
  
  ; Set the Registry view depdending on 32- or 64-bit Windows
  ${If} ${RunningX64}
    SetRegView 64
;    MessageBox MB_OK "64 bit Windows detected."
  ${EndIf}

  ; Copy the current version to the stack
  ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"

  ; If error reading registry for Java version
  IfErrors 0 +2
    MessageBox MB_YESNO "Java was not detected on your computer.  \
    Would you like to download Java now?" IDYES downloadJava IDNO done

  ; Compare the found Java version to the required version 
  StrCmp $0 ${JRE_VERSION} done

  ; Report only the common Java version, eg 7/8, not 1.7/1.8 
  ${StrTok} $1 $0 "." "1" "1"
  ${StrTok} $2 ${JRE_VERSION} "." "1" "1"

  MessageBox MB_YESNO "Java $1 detected on your computer.  Repast requires Java $2. \
  Would you like to download Java now?" IDYES downloadJava IDNO done

  ; Brings up the Java download page in a browser    
  downloadJava:
    ExecShell "open" "http://java.com/en/download/index.jsp"
  
  done:
FunctionEnd

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
  Call DetectJRE  ; First thing check if a suitable JRE is installed

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
