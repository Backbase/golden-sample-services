<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("emailVerifyTitle")}
    <#elseif section = "form">
      <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
        <select id="device" name="device">
            <#list device>
                <#items as listItem>
                  <option value="${listItem.deviceId}">
                      ${listItem.deviceId}
                  </option>
                </#items>
            </#list>
        </select>
        <div class="${properties.kcFormGroupClass!} ${properties.bbButtonContainerClass!}">
          <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
            <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
          </div>
        </div>
      </form>
    </#if>
</@layout.registrationLayout>