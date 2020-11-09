<!--
  ~ JBoss, Home of Professional Open Source.
  ~ Copyright (c) 2011, Red Hat, Inc., and individual contributors
  ~ as indicated by the @author tags. See the copyright.txt file in the
  ~ distribution for a full listing of individual contributors.
  ~
  ~ This is free software; you can redistribute it and/or modify it
  ~ under the terms of the GNU Lesser General Public License as
  ~ published by the Free Software Foundation; either version 2.1 of
  ~ the License, or (at your option) any later version.
  ~
  ~ This software is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ Lesser General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Lesser General Public
  ~ License along with this software; if not, write to the Free
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="${properties.kcHtmlClass!}">
<head>

    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>

    <title>Identity Console</title>
    <link rel="shortcut icon" href="${resourcesPath}/test.ico" type="image/x-icon" />

    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>
<body onload="loadBackground();welcomeInit();">
  <div class="${properties.bbWelcomePageClass!}">
    <div class="${properties.bbWelcomeBackgroundClass!}"></div>
    <div class="${properties.bbWelcomeBreakClass!}"></div>
    <div class="${properties.bbWelcomeInnerClass!}">
      <div class="${properties.bbWelcomePanelClass!}">
        <div class="${properties.bbWelcomeContainerClass!}">
          <div class="${properties.bbBannerSectionClass!}">
            <div class="${properties.bbLogoClass!}"></div>
          </div>
          <header class="${properties.bbWelcomeHeaderClass!}">
            <h1>Identity Console</h1>
          </header>
          <div class="${properties.bbWelcomeSubtitleClass!}">
            <span>Select the function you would like to perform</span>
          </div>

          <div class="${properties.bbWelcomeSectionClass!}">
            <div class="${properties.bbWelcomeSectionInnerClass!}">
              <#if successMessage?has_content>
                <div class="alert alert-success">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small">${successMessage}</span>
                    </div>
                  </div>
                </div>
              <#elseif errorMessage?has_content>
                <div class="alert alert-danger">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small">${errorMessage}</span>
                    </div>
                  </div>
                </div>
              </#if>

              <#if bootstrap>
                <#if localUser>
                  <div class="${properties.bbWelcomeSectionHeaderClass!}">
                    <h4 class="${properties.bbWelcomeSectionTitleClass!}">Administration Console</h4>
                    <div class="${properties.bbWelcomeSectionSubtitleClass!}">Please create an initial admin user to get started.</div>
                  </div>
                  <form method="post" class="${properties.bbWelcomeFormClass!}">
                    <div class="${properties.bbWelcomeFormInnerClass!}">
                      <div class="${properties.bbWelcomeFormGroupClass!}">
                        <label for="username" class="${properties.bbWelcomeFormLabelClass!}">Username</label>
                        <input id="username" class="${properties.bbWelcomeFormInputClass!}" name="username" />
                      </div>

                      <div class="${properties.bbWelcomeFormGroupClass!}">
                        <label for="password" class="${properties.bbWelcomeFormLabelClass!}">Password</label>
                        <input id="password" class="${properties.bbWelcomeFormInputClass!}" name="password" type="password" />
                      </div>

                      <div class="${properties.bbWelcomeFormGroupClass!}">
                        <label for="passwordConfirmation" class="${properties.bbWelcomeFormLabelClass!}">Password confirmation</label>
                        <input id="passwordConfirmation" class="${properties.bbWelcomeFormInputClass!}" name="passwordConfirmation" type="password" />
                      </div>

                      <input id="stateChecker" name="stateChecker" type="hidden" value="${stateChecker}" />
                    </div>

                    <div class="${properties.bbWelcomeFormButtonsClass!}">
                      <button id="create-button" type="submit" class="${properties.bbWelcomeFormButtonClass!}">Create user</button>
                    </div>
                  </form>
                <#else>
                  <div class="alert alert-danger">
                    <div class="alert-body">
                      <div class="alert-content">
                        <span class="small">You need local access to create the initial admin user.</span>
                      </div>
                    </div>
                  </div>
                  <p>
                    Open the <a href="http://localhost:8080/auth">Administration Console</a> or use the add-user-keycloak script.
                  </p>
                </#if>
              <#else>
                <div id="admin-console-button"
                  class="${properties.bbWelcomeSectionHeaderClass!} ${properties.bbWelcomeSectionSelectableClass!}"
                  tabindex="0"
                  role="button"
                  onclick="location.href='admin/'">
                  <h4 class="${properties.bbWelcomeSectionTitleClass!}">
                    Administration Console
                  </h4>
                  <div class="${properties.bbWelcomeSectionLinkClass!}">
                    <div class="${properties.bbWelcomeSectionLinkTextClass!}">
                      <div class="${properties.bbWelcomeSectionSubtitleClass!}">
                        Centrally manage all aspects of the Identity server
                      </div>
                    </div>
                    <div class="${properties.bbWelcomeSectionLinkIconClass!}">
                      <i aria-hidden="true" class="bb-icon bb-icon-arrow-forward bb-icon--primary"></i>
                    </div>
                  </div>
                </div>
              </#if>
            </div>
            <hr />
          </div>

          <div class="${properties.bbWelcomeSectionClass!}">
            <div class="${properties.bbWelcomeSectionInnerClass!}">
              <div id="identity-docs-button"
                class="${properties.bbWelcomeSectionHeaderClass!} ${properties.bbWelcomeSectionSelectableClass!}"
                tabindex="0"
                role="button"
                onclick="location.href='${properties.identityDocumentationUrl}'">
                <h4 class="${properties.bbWelcomeSectionTitleClass!}">
                  Identity Documentation
                </h4>
                <div class="${properties.bbWelcomeSectionLinkClass!}">
                  <div class="${properties.bbWelcomeSectionLinkTextClass!}">
                    <div class="${properties.bbWelcomeSectionSubtitleClass!}">
                      Installation, extension, adoption and release notes
                    </div>
                  </div>
                  <div class="${properties.bbWelcomeSectionLinkIconClass!}">
                    <i aria-hidden="true" class="bb-icon bb-icon-arrow-forward bb-icon--primary"></i>
                  </div>
                </div>
              </div>
            </div>
            <hr />
          </div>

          <div class="${properties.bbWelcomeSectionClass!}">
            <div class="${properties.bbWelcomeSectionInnerClass!}">
              <div id="keycloak-docs-button"
                class="${properties.bbWelcomeSectionHeaderClass!} ${properties.bbWelcomeSectionSelectableClass!}"
                tabindex="0"
                role="button"
                onclick="location.href='${properties.keycloakDocumentationUrl}'">
                <h4 class="${properties.bbWelcomeSectionTitleClass!}">
                  Keycloak Documentation
                </h4>
                <div class="${properties.bbWelcomeSectionLinkClass!}">
                  <div class="${properties.bbWelcomeSectionLinkTextClass!}">
                    <div class="${properties.bbWelcomeSectionSubtitleClass!}">
                      User Guide, Admin REST API and Javadocs
                    </div>
                  </div>
                  <div class="${properties.bbWelcomeSectionLinkIconClass!}">
                    <i aria-hidden="true" class="bb-icon bb-icon-arrow-forward bb-icon--primary"></i>
                  </div>
                </div>
              </div>
            </div>
            <hr />
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
