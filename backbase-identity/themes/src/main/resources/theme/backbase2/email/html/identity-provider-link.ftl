
<#outputformat "plainText">
  <#assign baseFont>
    font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif;
  </#assign>

  <#assign preHeaderStyles>
    box-sizing: border-box; display: none !important; ${baseFont} font-size: 1px; line-height: 1px; max-height: 0; max-width: 0; mso-hide: all; opacity: 0; overflow: hidden; visibility: hidden;
  </#assign>

  <#assign baseTableStyles>
    box-sizing: border-box; ${baseFont} margin: 0; padding: 0; width: 100%;
  </#assign>

  <#assign baseTableDataStyles>
    box-sizing: border-box; ${baseFont} word-break: break-word;
  </#assign>

  <#assign baseTableDataPaddedStyles>
    ${baseTableDataStyles} padding: 35px;
  </#assign>

  <#assign emailBodyStyles>
    -premailer-cellpadding: 0; -premailer-cellspacing: 0; border-bottom-color: #EDEFF2; border-bottom-style: solid; border-bottom-width: 1px; border-top-color: #EDEFF2; border-top-style: solid; border-top-width: 1px; word-break: break-word;
  </#assign>

  <#assign emailBodyTableStyles>
    box-sizing: border-box; ${baseFont} margin: 0 auto; padding: 0; width: 570px;
  </#assign>

  <#assign baseTextStyles>
    box-sizing: border-box; ${baseFont} line-height: 1.5em; margin-top: 0; color: #74787E;
  </#assign>

  <#assign headingStyles>
    ${baseTextStyles} font-size: 22px;
  </#assign>

  <#assign baseParagraphStyles>
    ${baseTextStyles} font-size: 16px;
  </#assign>

  <#assign baseFooterParagraphStyles>
    box-sizing: border-box; ${baseFont} margin: 0 auto; padding: 0; text-align: center; width: 570px;
  </#assign>

  <#assign baseFooterParagraphStyles>
    box-sizing: border-box; color: #AEAEAE; ${baseFont} font-size: 12px; line-height: 1.5em; margin-top: 0;
  </#assign>

  <#assign baseButtonStyles>
    -webkit-text-size-adjust: none; background: #22BC66; border-color: #22bc66; border-radius: 3px; border-style: solid; border-width: 10px 18px; box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16); box-sizing: border-box; color: #FFF; display: inline-block; ${baseFont} text-decoration: none;
  </#assign>

  <#assign nestedTableStyles>
    box-sizing: border-box; ${baseFont} margin: 30px auto; padding: 0; text-align: center; width: 100%;
  </#assign>
</#outputformat>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>${kcSanitize(msg("identityProviderLinkTitle", realmName))}</title>
  </head>

  <body style="-webkit-text-size-adjust: none; box-sizing: border-box; color: #74787E; font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; height: 100%; line-height: 1.4; margin: 0; width: 100% !important;" bgcolor="#F2F4F6">
    <style type="text/css">
      body {
      width: 100% !important; height: 100%; margin: 0; line-height: 1.4; background-color: #F2F4F6; color: #74787E; -webkit-text-size-adjust: none;
      }
      @media only screen and (max-width: 600px) {
        .email-body_inner {
          width: 100% !important;
        }
        .email-footer {
          width: 100% !important;
        }
      }
      @media only screen and (max-width: 500px) {
        .button {
          width: 100% !important;
        }
      }
    </style>
    <span class="preheader" style="${preHeaderStyles}">
      ${kcSanitize(msg("identityProviderPreheader", realmName))?no_esc}
    </span>
    <table class="email-wrapper" width="100%" cellpadding="0" cellspacing="0" style="${baseTableStyles}" bgcolor="#F2F4F6">
      <tr>
        <td align="center" style="${baseTableDataStyles}">
          <table class="email-content" width="100%" cellpadding="0" cellspacing="0" style="${baseTableStyles}">
            <tr>
              <td class="email-body" width="100%" cellpadding="0" cellspacing="0" style="${baseTableStyles} ${emailBodyStyles}" bgcolor="#FFFFFF">
                <table class="email-body_inner" align="center" width="570" cellpadding="0" cellspacing="0" style="${emailBodyTableStyles}" bgcolor="#FFFFFF">
                  <tr>
                    <td class="content-cell" style="${baseTableDataPaddedStyles}">
                      <h2 style="${headingStyles}" align="left">
                        ${kcSanitize(msg("emailUserIntro", user.firstName))}
                      </h2>

                      <p style="${baseParagraphStyles}" align="left">
                        ${kcSanitize(msg("identityProviderLinkBodyHtml", identityProviderAlias, realmName, identityProviderContext.username, link, linkExpiration, linkExpirationFormatter(linkExpiration)))?no_esc}
                      </p>

                      <p style="${baseParagraphStyles}" align="left">
                        ${msg("emailFromMessageHtml")?no_esc}
                      </p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td style="${baseTableDataStyles}">
                <table class="email-footer" align="center" width="570" cellpadding="0" cellspacing="0" style="${baseFooterParagraphStyles}">
                  <tr>
                    <td class="content-cell" align="center" style="${baseTableDataPaddedStyles}">
                      <p class="sub align-center" style="${baseFooterParagraphStyles}" align="center">
                        ${msg("emailFromFooterHtml")?no_esc}
                      </p>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </body>
</html>
