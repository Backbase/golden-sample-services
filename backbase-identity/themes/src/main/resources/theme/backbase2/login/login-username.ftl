<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=social.displayInfo displayWide=(realm.password && social.providers??); section>
    <#if section = "header">
        <#if realm.name = "master">
            ${msg("logInHeader")}
        <#else>
            ${msg("onlineBankingLogInHeader")}
        </#if>
    <#elseif section = "form">
    <div class="${properties.bbFormMessageClass!}">
        <span>${msg("logInFormMessage")}</span>
    </div>
    <div id="kc-form" <#if realm.password && social.providers??>class="${properties.kcContentWrapperClass!}"</#if>>
        <div id="kc-form-wrapper" <#if realm.password && social.providers??>class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}"</#if>>
            <#if realm.password>
                <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                    <div class="${properties.kcFormGroupClass!}">
                        <label for="username" class="${properties.kcLabelClass!}"><#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if></label>

                        <#if usernameEditDisabled??>
                            <input id="username" class="${properties.kcInputClass!}" name="username" value="${(login.username!'')}" type="text" disabled />
                        <#else>
                            <input id="username" class="${properties.kcInputClass!}" name="username" value="${(login.username!'')}"  type="text" autofocus autocomplete="off" />
                        </#if>
                    </div>

                    <#if realm.resetPasswordAllowed>
                        <div class="${properties.kcFormOptionsWrapperClass!} ${properties.bbFormGroupProblemFieldClass!}">
                            <span><a href="${url.loginForgotUsernameUrl}">${msg("loginUsernameForgotten")}</a></span>
                        </div>
                    </#if>

                    <#if realm.rememberMe && !usernameEditDisabled??>
                        <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                            <div id="kc-form-options">
                                <div class="checkbox">
                                    <label class="${properties.bbFormCheckboxLabelClass!}">
                                        <#if login.rememberMe??>
                                            <input class="${properties.bbFormCheckboxClass!}" id="rememberMe" name="rememberMe" type="checkbox" checked>
                                        <#else>
                                            <input class="${properties.bbFormCheckboxClass!}" id="rememberMe" name="rememberMe" type="checkbox">
                                        </#if>
                                        <span class="bb-input-checkbox__content">${msg("loginUsernameRememberMe")}</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </#if>

                    <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                        <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                            <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("loginUsernameContinue")}"/>
                        </div>
                    </div>

                    <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                        <div id="kc-registration" class="${properties.bbEnrollContainerClass!}">
                            <span>${msg("noAccount")} <a href="${url.registrationUrl}">${msg("doRegister")}</a></span>
                        </div>
                    </#if>
                </form>
            </#if>
        </div>
        <#if realm.password && social.providers??>
            <div id="kc-social-providers" class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}">
                <ul class="${properties.kcFormSocialAccountListClass!} <#if social.providers?size gt 4>${properties.kcFormSocialAccountDoubleListClass!}</#if>">
                    <#list social.providers as p>
                        <li class="${properties.kcFormSocialAccountListLinkClass!}"><a href="${p.loginUrl}" id="zocial-${p.alias}" class="zocial ${p.providerId}"> <span>${p.displayName}</span></a></li>
                    </#list>
                </ul>
            </div>
        </#if>
    </div>
    </#if>

</@layout.registrationLayout>
