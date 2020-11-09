# backbase (theme)
This is a theme that implements the backbase-theme css module

**Important: npm install the common package.json for required dependencies**

## Common
Package.json added that pulls in the bb-theme module

## Login

The following templates were modified in some way (generally prefering minor edits):
### template.ftl
  * Added logo property `bbLogoClass`
  * Fixed error message displaying and class to match bootstrap `.alert-danger`
  * added `bbPageHeaderClass` to H1 heading to allow custom classes
  * added `bbLoginInnerClass` and `bbCardContainerClass` element containers for design
  * moved `#kc-page-title` below `#kc-locale` element.

### login.ftl
  * Re-organised `if` statements to avoid empty divs with classes that add spacing when not expected
  * Added `bbButtonContainerClass` to the action button container
  * Added link for forgotten username, will appear when forgot password is enabled

### login-reset-password.ftl
  * removed `info` section and moved its content to `form` section
  * moved `kc-form-options` element to be the first element
  * set `displayInfo=false`
  * added `${properties.bbButtonContainerClass!}` to submit button container

### login-update-password.ftl
  * set `displayInfo=false`
  * added `${properties.bbButtonContainerClass!}` to submit button container

### login-update-profile.ftl
  * added `${properties.bbButtonContainerClass!}` to submit button container

---

The following templates are custom to the backbase theme:
### login-forgot-username.ftl
  * a custom template for recovering a forgotten username

---

The following were copied from "base" and remain unchanged, here for historical reasons in the event of an upgrade
  * `cli_splash.ftl`
  * `code.ftl`
  * `error.ftl`
  * `info.ftl`
  * `login-config-totp-text.ftl`
  * `login-config-totp.ftl`
  * `login-idp-link-confirm.ftl`
  * `login-idp-link-email.ftl`
  * `login-oauth-grant.ftl`
  * `login-page-expired.ftl`
  * `login-totp.ftl`
  * `login-update-profile.ftl`
  * `login-verify-email-code-text.ftl`
  * `login-verify-email.ftl`
  * `login-x509-info.ftl`
  * `register.ftl`
  * `terms.ftl`


## Admin
The following were copied from "base" and remain unchanged, here for historical reasons in the event of an upgrade:
  * `index.ftl`

## Email
The themes were custom built to allow easier editing. They contain a number of variables for use in them and the messages file contains all relevant text.

## Account
Adjusted the HTMl for the menus so that it better displays a layout that looks similar to the admin screens `template.ftl`
