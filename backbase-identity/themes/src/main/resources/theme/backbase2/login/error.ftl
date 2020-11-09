<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        <#if errorHeader?has_content>
            ${kcSanitize(msg(errorHeader))?no_esc}
        <#else>
            ${msg("errorTitle")}
        </#if>
    <#elseif section = "form">
        <div id="kc-error-message">
            <div class="${properties.bbFormMessageClass!}">
                <span class="instruction">${kcSanitize(message.summary)?no_esc}</span>
            </div>
            <#if client?? && client.baseUrl?has_content>
                <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <button
                            id="backToApplication" 
                            class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                            type="button"
                            onclick="location.assign('${client.baseUrl}')">
                            <div class="bb-stack bb-stack--spacing-lg">
                                <div class="bb-stack__item bb-stack__item--spacing-sm">
                                    <i aria-hidden="true" class="bb-icon bb-icon-arrow-back bb-icon--md align-middle"></i>
                                </div>
                                <div class="bb-stack__item">${kcSanitize(msg("backToLogin"))?no_esc}</div>
                            </div>
                        </button>
                    </div>
                </div>
            </#if>
        </div>
    </#if>
</@layout.registrationLayout>