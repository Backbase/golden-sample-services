<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        <#if messageHeader??>
        ${messageHeader}
        <#else>
        ${message.summary}
        </#if>
    <#elseif section = "form">
    <div id="kc-info-message">
        <div class="${properties.bbFormMessageClass!}">
            <span class="instruction">${message.summary}<#if requiredActions??><#list requiredActions>: <b><#items as reqActionItem>${msg("requiredAction.${reqActionItem}")}<#sep>, </#items></b></#list><#else></#if></span>
        </div>
        <#if skipLink??>
        <#else>
            <#if pageRedirectUri??>
                <p>
                    <a href="${pageRedirectUri}">
                        <i aria-hidden="true" class="bb-icon bb-icon-arrow-back bb-icon--sm align-middle"></i>
                        ${kcSanitize(msg("backToApplication"))?no_esc}
                    </a>
                </p>
            <#elseif actionUri??>
                <p>
                    <a href="${actionUri}">
                        ${kcSanitize(msg("proceedWithAction"))?no_esc}
                        <i aria-hidden="true" class="bb-icon bb-icon-arrow-forward bb-icon--sm align-middle"></i>
                    </a>
                </p>
            <#elseif client.baseUrl??>
                <p>
                    <a href="${client.baseUrl}">
                        <i aria-hidden="true" class="bb-icon bb-icon-arrow-back bb-icon--sm align-middle"></i>
                        ${kcSanitize(msg("backToApplication"))?no_esc}
                    </a>
                </p>
            </#if>
        </#if>
    </div>
    </#if>
</@layout.registrationLayout>