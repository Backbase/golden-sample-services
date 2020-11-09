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

<body class="${properties.kcBodyClass!}">
  <div class="${properties.kcLoginClass!}">
    <div class="${properties.bbLoginInnerClass!}">
      <div class="${properties.bbCardContainerClass!}">
        <div id="kc-header" class="${properties.kcHeaderClass!}">
          <div id="kc-header-wrapper" class="${properties.kcHeaderWrapperClass!}">${msg("loginTitleHtml",(realm.displayNameHtml!''))?no_esc}</div>
        </div>
        <div class="${properties.kcFormCardClass!} <#if displayWide>${properties.kcFormCardAccountClass!}</#if>">
          <div class="${properties.bbLogoClass}"></div>
          <header class="${properties.kcFormHeaderClass!}">
            <h1 id="kc-page-title" class="${properties.bbPageHeaderClass}"><#nested "header"></h1>
            <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
              <div id="kc-locale">
                <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                  <div class="kc-dropdown" id="kc-locale-dropdown">
                    <a href="#" id="kc-current-locale-link">${locale.current}</a>
                    <ul>
                      <#list locale.supported as l>
                        <li class="kc-dropdown-item"><a href="${l.url}">${l.label}</a></li>
                      </#list>
                    </ul>
                  </div>
                </div>
              </div>
            </#if>
          </header>

          <div id="kc-content">
            <div id="kc-content-wrapper">

              <#if displayMessage && message?has_content>
                <#if message.type != 'error'>
                  <div class="alert alert-${message.type} ${properties.bbAlert}">
                    <#if message.type = 'success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                    <#if message.type = 'warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                    <#if message.type = 'info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                    <span class="kc-feedback-text">${message.summary?no_esc}</span>
                  </div>
                </#if>

                <#if message.type == 'error'>
                  <div class="alert alert-danger ${properties.bbAlert}">
                    <span class="${properties.kcFeedbackErrorIcon!}"></span>
                    <span class="kc-feedback-text">${message.summary?no_esc}</span>
                  </div>
                </#if>
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
    </div>
  </div>
</body>
</html>
</#macro>
