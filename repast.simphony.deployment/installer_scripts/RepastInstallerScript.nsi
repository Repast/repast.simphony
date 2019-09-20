; RepastInstallerScript.nsi
;
; This is an installer script for Repast Simphony
;
;--------------------------------

!include "MUI.nsh"        ; user the modern UI
!include nsDialogs.nsh
!include "LogicLib.nsh"   ; Library for logical statements 
!include "StrFunc.nsh"    ; String functions
!include "x64.nsh"        ; Macros for x64 machines

!define VERSION "2.7"     ; Repast Version

!define JAVA_HOME "jdk11"                          ; The included Java root dir
!define JAVA_BIN "eclipse\${JAVA_HOME}\bin\javaw"  ; path to bundled VM
!define VM_ARGS "-vm ${JAVA_BIN}"                  ; vm arg for eclipse to use bundled jdk

Var /GLOBAL eclipse_params
Var /GLOBAL javabin

; The name of the installer
Name "Repast Simphony ${VERSION}"

; The file to write
OutFile "Repast-Simphony-${VERSION}-win64.exe"

; The default installation directory.  The $PROFILE variable is the user's home dir
InstallDir $PROFILE\RepastSimphony-${VERSION}

; Request Administrator level application privileges when copying files
#RequestExecutionLevel admin

;--------------------------------

; Pages
!define MUI_PAGE_CUSTOMFUNCTION_PRE WelcomePageSetupLinkPre
!define MUI_PAGE_CUSTOMFUNCTION_SHOW WelcomePageSetupLinkShow
!define MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE repast-license.txt
Page Custom JavaInfoPage
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------

; The stuff to install
Section "Repast Simphony" Section_Repast

  SectionIn RO ; Read-only - user can't unselect core files
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Install the Repast core files
  File /r eclipse
  File batch_runner.jar
  File run_batch_runner.bat
  File repast-license.txt
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}" "DisplayName" "Repast Simphony ${VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}" "UninstallString" '"$INSTDIR\uninstall.exe"'

  ; Used for enterprise remote uninstall
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}" "QuietUninstallString" '"$INSTDIR\uninstall.exe"'
  
  ; Removes uninstall options for modify and repair (only allow uninstall)
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}" "NoRepair" 1
  
  WriteUninstaller "uninstall.exe"
  
SectionEnd

Section "Java 11" Section_Java

  ; Set output path for the JDK to the installation\eclipse directory.
  SetOutPath $INSTDIR\eclipse
  
  ; Install the JDK
  File /r ${JAVA_HOME}
  
  ; add the eclipse -vm option to use the bundled JDK
  StrCpy $eclipse_params "${VM_ARGS}" 
  
  ; Define the Java VM bin to the bundled JDK
  StrCpy $javabin "${JAVA_BIN}"
  
SectionEnd

Section "Documentation" Section_Doc
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Install the Repast core files (Eclipse)
  File /r docs
SectionEnd

Section "Example Models" Section_Models
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Install the Repast core files (Eclipse)
  File /r models

SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts" Section_Shortcuts
 
  ; Install for all users
  SetShellVarContext all
  
  CreateDirectory "$SMPROGRAMS\RepastSimphony ${VERSION}"
  CreateDirectory "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation"

  ; CreateShortCut link.lnk target.file [parameters [icon.file [icon_index_number [start_options [keyboard_shortcut [description]]]]]]
  
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Repast Simphony.lnk" "$INSTDIR\eclipse\eclipse.exe" "$eclipse_params" "$INSTDIR\eclipse\eclipse.exe" 0
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Batch Runner.lnk" "$INSTDIR\run_batch_runner.bat" "$javabin"
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\UnInstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0  

  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\ReLogo Getting Started.lnk" "$INSTDIR\docs\ReLogoGettingStarted.pdf" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Java Getting Started.lnk" "$INSTDIR\docs\RepastJavaGettingStarted.pdf" 
  
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Reference.lnk" "$INSTDIR\docs\RepastReference\RepastReference.html" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Simphony API.lnk" "$INSTDIR\docs\RepastSimphonyAPI\index.html" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\ReLogo Primitives Quick Reference.lnk" "$INSTDIR\docs\RepastSimphonyAPI\ReLogoPrimitives.html" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Simphony Statecharts Guide.lnk" "$INSTDIR\docs\Statecharts.pdf" 
  
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Simphony Data Collection.lnk" "$INSTDIR\docs\DataCollection.pdf" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Batch Getting Started.lnk" "$INSTDIR\docs\RepastBatchRunsGettingStarted.pdf" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Model Testing Guide.lnk" "$INSTDIR\docs\RepastModelTesting.pdf" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Simphony FAQ.lnk" "$INSTDIR\docs\RepastFAQ\RepastFAQ.html" 
  
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Reference (online).lnk" "https://repast.github.io/docs/RepastReference/RepastReference.html" 
  CreateShortCut "$SMPROGRAMS\RepastSimphony ${VERSION}\Documentation\Repast Simphony FAQ (online).lnk" "https://repast.github.io/docs/RepastFAQ/RepastFAQ.html" 
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  ; Install for all users
  SetShellVarContext all
  
  ; Remove registry keys for uninstall
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\RepastSimphony ${VERSION}"

  ; Remove program group dir
  RMDir /r "$SMPROGRAMS\RepastSimphony ${VERSION}"
  
  ; Remove the install dir and the docs and eclipse sub-dirs 
  DELETE $INSTDIR\*.*
  RMDir /r $INSTDIR\docs
  RMDir /r $INSTDIR\eclipse
  RMDir /r $INSTDIR\models
  RMDir /r $INSTDIR

SectionEnd

; Section descriptions that appear in the compnents to install page
LangString DESC_Section_Repast ${LANG_ENGLISH} "Installs the core Repast libraries (required)."
LangString DESC_Section_Java ${LANG_ENGLISH} "Installs the include Java Development Kit. \ 
	It is recommended to install the included Java unless you are certain that you have a compatible version of Java already installed."
LangString DESC_Section_Doc ${LANG_ENGLISH} "Installs the Repast documentation."
LangString DESC_Section_Models ${LANG_ENGLISH} "Installs the Repast demo models."
LangString DESC_Section_Shortcuts ${LANG_ENGLISH} "Installs the Repast program and documents shortcuts."

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${Section_Repast} $(DESC_Section_Repast)
  !insertmacro MUI_DESCRIPTION_TEXT ${Section_Java} $(DESC_Section_Java)
  !insertmacro MUI_DESCRIPTION_TEXT ${Section_Doc} $(DESC_Section_Doc)
  !insertmacro MUI_DESCRIPTION_TEXT ${Section_Models} $(DESC_Section_Models)
  !insertmacro MUI_DESCRIPTION_TEXT ${Section_Shortcuts} $(DESC_Section_Shortcuts)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

; Custom page for Java installation information
LangString JAVA_PAGE_TITLE ${LANG_ENGLISH} "Java Information"
LangString JAVA_PAGE_SUBTITLE ${LANG_ENGLISH} "Please read carefully"

Var Dialog
Var Text

LangString JAVA_INFO ${LANG_ENGLISH} "Java version 8 or greater is required to run Repast.$\r$\n$\r$\n\
The default installation components include a compatible version of Java.  \
If you are unsure if Java 8 or later is already installed on your system, use the \
default installation settings and Java will be installed for you. $\r$\n$\r$\n\
The included Java runtime will not conflict with an existing Java installation on your system.$\r$\n$\r$\n\
Please see the Repast Quick Start guide for more information about Java."

Function JavaInfoPage
  !insertmacro MUI_HEADER_TEXT $(JAVA_PAGE_TITLE) $(JAVA_PAGE_SUBTITLE)

  ; Create a simple dialog on this page with a single lable with the Java info.  
  nsDialogs::Create 1018
	Pop $Dialog

	${If} $Dialog == error
		Abort
	${EndIf}

	${NSD_CreateLabel} 0 0 100% 100u $(JAVA_INFO)
	Pop $Text
	
	nsDialogs::Show
FunctionEnd

Function WelcomePageSetupLinkPre
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Settings" "Numfields" "4" ; increase counter
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 3" "Bottom" "122" ; limit size of the upper label
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Type" "Link"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "Text" "https://repast.github.io/"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "ioSpecial.ini" "Field 4" "State" "https://repast.github.io/"
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
