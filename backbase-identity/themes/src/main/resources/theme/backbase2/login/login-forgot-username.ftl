<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=false; section>
    <#if section = "header">
        ${msg("emailForgotUsernameTitle")}
    <#elseif section = "form">
        <div class="${properties.bbFormMessageClass!}">
            <span>${msg("emailUsernameInstruction")}</span>
        </div>

        <form id="kc-forgot-username-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="email" class="${properties.kcLabelClass!}">${msg("email")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!}">
                    <input type="text" id="email" name="email" class="${properties.kcInputClass!}" autofocus/>
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
                </div>
            </div>

            <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                <div class="${properties.kcFormOptionsWrapperClass!}">
                    <p>
                        <a href="${url.loginUrl}">
                            <i aria-hidden="true" class="bb-icon bb-icon-arrow-back bb-icon--sm align-middle"></i>
                            ${kcSanitize(msg("backToLogin"))?no_esc}
                        </a>
                    </p>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>
