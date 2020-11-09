<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=social.displayInfo displayWide=(realm.password && social.providers??); section>
    <#if section = "header">
        ${msg("loginPasswordHeader")}
    <#elseif section = "form">
    <div class="${properties.bbFormMessageClass!}">
        <span>${msg("loginPasswordFormMessage")}</span>
    </div>
    <div id="kc-form">
        <div id="kc-form-wrapper">
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">

                <div class="${properties.kcFormGroupClass!}">
                    <label for="password" class="${properties.kcLabelClass!}">${msg("password")}</label>
                    <input id="password" class="${properties.kcInputClass!}" name="password" type="password" autofocus autocomplete="off" />
                </div>

                <#if realm.resetPasswordAllowed>
                    <div class="${properties.kcFormOptionsWrapperClass!} ${properties.bbFormGroupProblemFieldClass!}">
                        <span><a href="${url.loginResetCredentialsUrl}">${msg("loginPasswordForgotten")}</a></span>
                    </div>
                </#if>

                <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("loginPasswordContinue")}"/>
                    </div>
                </div>
            </form>
        </div>
      </div>
    </#if>

</@layout.registrationLayout>
