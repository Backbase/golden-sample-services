<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("deviceSelectTitle")}
    <#elseif section = "form">
        <div class="${properties.bbFormMessageClass!}">
            <span>${msg("deviceListOrder")}</span>
        </div>
        <form id="bb-device-list-form" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="bb-dropdown-btn-group-wrapper">
                    <input hidden name="device" value="" />
                    <button id="bb-device-dropdown-button"
                        class="dropdown-toggle btn-secondary btn-sm btn"
                        aria-label="${msg("selectDeviceAriaLabel")}"
                        type="button">
                        <div class="dropdown-menu-toggle-button__content">
                            <span id="bb-selected-device"></span>
                            <i aria-hidden="true" class="bb-icon bb-icon-caret-down bb-icon--lg bb-icon--cropped"></i>
                        </div>
                    </button>
                    <div class="dropdown">
                        <div class="dropdown-menu" role="menu" id="bb-device-dropdown-menu">
                            <#list device as item>
                                <a class="dropdown-item" onclick="deviceListController.setDevice(this)" value="${item.deviceId}" tabindex="0">
                                    <#if item.friendlyName?has_content>
                                        ${item.friendlyName}
                                    <#elseif item.vendor?has_content && item.model?has_content>
                                        ${item.vendor} ${item.model}
                                    <#else>
                                        ${msg("deviceDefaultName")}
                                    </#if>
                                </a>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
            <div class="${properties.bbDeviceListSupportTextClass!}">
                ${msg("deviceListSupportText")}
            </div>
            <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input
                        class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                        type="submit"
                        value="${msg("deviceListSubmit")}"/>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>