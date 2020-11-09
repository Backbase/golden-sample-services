<div class="${properties.bbCheckDevicePageClass!}">
<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("checkDeviceHeader")}
    <#elseif section = "form">
        <script>
            const oobAuthPushCheckDevice = {
                resendAttemptsLeft: ${resendAttemptsLeft},
                nextResendSeconds: ${nextResendSeconds},
            };
        </script>
        <div class="bb-subtitle bb-block bb-block--lg">
            <span>${msg("checkDeviceSupportText")}</span>
        </div>

        <div class="bb-block bb-block--lg bb-stack">
            <div class="bb-stack__item">
                <i aria-hidden="true" class="bb-icon bb-icon--xl bb-icon-phone-iphone"></i>
            </div>
            <div class="bb-stack__item bb-stack__item--fill">
                <div class="bb-subtitle break-word">
                    <span>
                        <#if device.friendlyName?has_content>
                            ${device.friendlyName}
                        <#else>
                            ${msg("deviceDefaultName")}
                        </#if>
                    </span>
                </div>
                <small class="bb-subheader bb-text-support break-word">
                    <#if device.vendor?has_content && device.model?has_content>
                        ${device.vendor} ${device.model}
                    <#else>
                        ${msg("deviceUnknownVendorModel")}
                    </#if>
                </small>
            </div>
        </div>

        <div class="bb-subtitle bb-text-bold bb-block bb-block--sm">
            <span>${msg("checkDeviceNotReceivedText")}</span>
        </div>

        <#if resendAttemptsLeft?has_content && resendAttemptsLeft gt 0>
            <div id="bb-check-device-countdown-info">
                <div class="bb-subtitle bb-block bb-block--lg">
                    <span>${kcSanitize(msg("checkDeviceCountdownText"))?no_esc}</span>
                </div>

                <div class="bb-progress-bar bb-block bb-block--lg">
                    <div class="progress" style="height: 0.5rem">
                        <div 
                            id="bb-check-device-progress-bar"
                            aria-valuemin="0"
                            class="bg-primary progress-bar"
                            aria-valuenow="0"
                            aria-valuemax="${nextResendSeconds}"
                            style="width: 0%">
                        </div>
                    </div>
                </div>
            </div>
        </#if>

        <#if resendAttemptsLeft?has_content && resendAttemptsLeft gt 0>
            <form id="bb-check-device-resend-form"
                class="bb-block bb-block--lg"
                onsubmit="return checkDeviceController.validateForm()"
                action="${url.loginAction}"
                method="post"
                hidden>
                <input name="oob-authn-action" value="pn-resend" hidden>
                <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
                    <div class="${properties.kcFormButtonsClass!}">
                        <button
                            class="${properties.kcButtonClass!} ${properties.kcButtonSecondaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                            type="submit">
                            ${msg("checkDeviceResendButtonText")}
                        </button>
                    </div>
                </div>
                <small id="bb-check-device-resend-locked" class="bb-text-danger" hidden>
                    ${msg("checkDeviceResendLockedText")}
                </small>
            </form>
        <#else>
            <div class="alert alert-danger">
                <div class="alert-body">
                    <div class="alert-content">
                        <span class="small kc-feedback-text">
                            ${kcSanitize(msg("checkDeviceNoResendAlertText"))}
                        </span>
                    </div>
                </div>
            </div>
        </#if>

        <form id="bb-check-device-continue-form" action="${url.loginAction}" method="post" hidden>
            <input name="oob-authn-action" value="confirmation-continue">
        </form>

        <div id="bb-check-device-modal-header" hidden>
            <h2 aria-level="2" role="heading">${msg("checkDeviceErrorModalHeader")}</h2>
        </div>
        <div id="bb-check-device-modal-body" hidden>
            <div class="bb-block bb-block--lg">
                ${msg("checkDeviceErrorModalBody")}
            </div>
            <div class="bb-button-bar">
                <button
                    class="bb-button-bar__button btn-primary btn btn-md"
                    type="button"
                    onclick="location.href='${url.loginRestartFlowUrl}'">
                    ${msg("checkDeviceErrorModalPrimaryButton")}
                </button>
                <button
                    class="bb-button-bar__button btn-secondary btn btn-md"
                    type="button"
                    onclick="location.reload()">
                    ${msg("checkDeviceErrorModalSecondaryButton")}
                </button>
            </div>
        </div>
    </#if>

</@layout.registrationLayout>
</div>