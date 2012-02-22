; RepastInstallerScript.nsi
;
; This is an installer script for Repast Simphony
;
;--------------------------------

; user the modern UI
!include "MUI.nsh"

!define VERSION "2.0"

; The name of the installer
Name "Repast Simphony ${VERSION}"

; The file to write
OutFile "Repast-Simphony-${VERSION}-win.exe"

; The default installation directory
;InstallDir $PROGRAMFILES\RepastSimphony-${VERSION}
; changed to avoid user permissions problems with $PROGRAMFILES
InstallDir C:\RepastSimphony-${VERSION}

; Request application privileges for Windows Vista
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

  CreateDirectory "$SMPROGRAMS\RepastSimphony-${VERSION}"
  
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Simphony.lnk" "$INSTDIR\eclipse\eclipse.exe" "" "$INSTDIR\eclipse\eclipse.exe" 0  
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\ReLogo Getting Started.lnk" "$INSTDIR\docs\ReLogoGettingStarted.pdf" "" "$INSTDIR\docs\ReLogoGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Java Getting Started.lnk" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" "" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Flowchart Getting Started.lnk" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" "" "$INSTDIR\docs\RepastFlowGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Reference.lnk" "$INSTDIR\docs\RepastReference.pdf" "" "$INSTDIR\docs\RepastReference.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Java API.lnk" "$INSTDIR\docs\RepastJavaAPI\index.html" "" "$INSTDIR\docs\RepastJavaAPI\index.html" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\ReLogo Primitives Quick Reference.lnk" "$INSTDIR\docs\RepastJavaAPI\ReLogoPrimitives.html" "" "$INSTDIR\docs\RepastJavaAPI\ReLogoPrimitives.html" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast FAQ.lnk" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" "" "$INSTDIR\docs\RepastSimphonyFAQ.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Parameter Sweeps Getting Started.lnk" "$INSTDIR\docs\RepastParameterSweepsGettingStarted.pdf" "" "$INSTDIR\docs\RepastParameterSweepsGettingStarted.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\Repast Data Collection.lnk" "$INSTDIR\docs\DataCollection.pdf" "" "$INSTDIR\docs\DataCollection.pdf" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony-${VERSION}\UnInstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0  
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony-${VERSION}"
  DeleteRegKey HKLM SOFTWARE\RepastSimphony-${VERSION}

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\RepastSimphony-${VERSION}\*.*"

  ; Remove program group dir
  RMDir /r "$SMPROGRAMS\RepastSimphony-${VERSION}"
  
  ; Only remove files in the install dir and the docs and eclipse sub-dirs so user 
  ; won't accidentally delete workspace files
  DELETE $INSTDIR\*.*
  RMDir /r $INSTDIR\docs
  RMDir /r $INSTDIR\eclipse
  RMDir /r $INSTDIR\models

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
