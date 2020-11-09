<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayWide=false>
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
    <title>${msg("loginTitle",(realm.displayName!''))}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>
<body onload="loadBackground();loginInit();">
  <div class="${properties.kcLoginClass!}">
    <div class="${properties.bbLoginBackgroundClass!}"></div>
    <div class="${properties.bbLoginBreakClass!}"></div>
    <div class="${properties.bbLoginInnerClass!}">
      <div class="${properties.bbCardContainerClass!}">
        <div id="kc-header" class="${properties.kcHeaderClass!}">
          <div id="kc-header-wrapper" class="${properties.kcHeaderWrapperClass!}">${msg("loginTitleHtml",(realm.displayNameHtml!''))?no_esc}</div>
        </div>
        <div class="${properties.bbFormContainerClass!} <#if displayWide>${properties.kcFormCardAccountClass!}</#if>">
          <div class="${properties.bbBannerSectionClass!}">
            <div class="${properties.bbLogoClass!}"></div>
            <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
              <div class="${properties.bbLocaleClass!}" id="kc-locale">
                <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                  <div class="bb-dropdown-btn-group-wrapper" id="kc-locale-dropdown">
                    <button class="dropdown-toggle btn-secondary btn-sm btn" href="#" id="kc-current-locale-link">
                      <div class="dropdown-menu-toggle-button__content">
                        <span>${locale.current}</span>
                        <i aria-hidden="true" class="bb-icon bb-icon-caret-down bb-icon--lg bb-icon--cropped"></i>
                      </div>
                    </button>
                    <div class="dropdown dropleft">
                      <div class="dropdown-menu" role="menu" id="kc-locale-dropdown-menu">
                        <#list locale.supported as l>
                          <a class="dropdown-item" href="${l.url}">${l.label}</a>
                        </#list>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </#if>
          </div>
          <header class="${properties.kcFormHeaderClass!}">
            <h1 id="kc-page-title"><#nested "header"></h1>
          </header>

          <#if displayMessage && message?has_content>
            <div class="${properties.bbFeedbackContainerClass!}">
              <#if message.type = 'success'>
                <span class="${properties.kcFeedbackSuccessIcon!}"></span>
                <div class="alert alert-success">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small"><label class="kc-feedback-text">${message.summary?no_esc}</label></span>
                    </div>
                  </div>
                </div>
              </#if>
              <#if message.type = 'warning'>
                <span class="${properties.kcFeedbackWarningIcon!}"></span>
                <div class="alert alert-warning">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small"><label class="kc-feedback-text">${message.summary?no_esc}</label></span>
                    </div>
                  </div>
                </div>
              </#if>
              <#if message.type = 'info'>
                <span class="${properties.kcFeedbackInfoIcon!}"></span>
                <div class="alert alert-info">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small"><label class="kc-feedback-text">${message.summary?no_esc}</label></span>
                    </div>
                  </div>
                </div>
              </#if>
              <#if message.type == 'error'>
                <span class="${properties.kcFeedbackErrorIcon!}"></span>
                <div class="alert alert-danger">
                  <div class="alert-body">
                    <div class="alert-content">
                      <span class="small"><label class="kc-feedback-text">${message.summary?no_esc}</label></span>
                    </div>
                  </div>
                </div>
              </#if>
            </div>
          </#if>

          <#nested "form">

          <#if displayInfo>
            <div id="kc-info" class="${properties.kcSignUpClass!}">
              <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                <#nested "info">
              </div>
            </div>
          </#if>
        </div>
      </div>
    </div>
  </div>

  <div id="bb-modal" hidden>
    <div aria-hidden="true" class="modal-backdrop fade show"></div>
    <div role="dialog" tabindex="-1" class="modal fade show"  style="display: block" aria-modal="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" id="bb-modal-header"></div>
                <div class="modal-body" id="bb-modal-body"></div>
            </div>
        </div>
    </div>
  </div>
</body>
</html>
</#macro>
