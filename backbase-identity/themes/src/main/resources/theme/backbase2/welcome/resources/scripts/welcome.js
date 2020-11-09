function welcomeInit() {
  const spaceBarKey = 32;

  function addKeyupListener(element) {
    if (element) {
      element.addEventListener('keyup', function(event) {
        if (event.which === spaceBarKey) {
          element.click();
        }
      });
    }
  }

  addKeyupListener(document.getElementById('admin-console-button'));
  addKeyupListener(document.getElementById('identity-docs-button'));
  addKeyupListener(document.getElementById('keycloak-docs-button'));
}