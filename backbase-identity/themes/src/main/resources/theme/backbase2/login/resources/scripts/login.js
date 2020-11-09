const loginController = (function() {
  function init() {
    if (modalController) {
      modalController.init();
    }
  
    if (dropdownController) {
      dropdownController.createDropdownHandler('kc-locale-dropdown-menu', 'kc-current-locale-link');
    }
  
    if (deviceListController) {
      deviceListController.init();
    }
  
    if (checkDeviceController) {
      checkDeviceController.init();
    }
  }

  return {
    init: init,
  }
})()

function loginInit() {
  if (loginController) {
    loginController.init();
  }
}
