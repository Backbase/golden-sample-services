const deviceListController = (function() {
  const buttonId = 'bb-device-dropdown-button';
  const menuId = 'bb-device-dropdown-menu';
  const formId = 'bb-device-list-form';
  const selectedDeviceElementId = 'bb-selected-device';

  function isPagePresent() {
    return !!(document.getElementById(formId));
  }

  function getDeviceFromElement(device) {
    const deviceId = device.getAttribute('value');
    const displayName = device.textContent.trim();
    return { deviceId: deviceId, displayName: displayName };
  }

  function updateDeviceOnDom(device) {
    const deviceForm = document.getElementById(formId);
    const selectedDeviceElement = document.getElementById(selectedDeviceElementId);
    deviceForm.device.value = device.deviceId;
    selectedDeviceElement.textContent = device.displayName;
  }

  function setDevice(device) {
    updateDeviceOnDom(getDeviceFromElement(device));
    document.getElementById(buttonId).focus();
  }

  function onDeviceListKeyUp(event) {
    if (event.key === 'Enter' || event.key === ' ') {
      setDevice(device);
    }
  }

  function setDeviceList(devices) {
    const listElement = document.getElementById(menuId);
    for (let i = 0; i < listElement.children.length; i++) {
      const device = listElement.children[i];
      devices.push(getDeviceFromElement(device));
      device.addEventListener('keyup', onDeviceListKeyUp);
    }
  }

  function init() {
    if (!isPagePresent()) {
      return;
    }

    const devices = [];
    if (dropdownController) {
      dropdownController.createDropdownHandler(menuId, buttonId, true);
    }

    setDeviceList(devices);
    if (devices) {
      updateDeviceOnDom(devices[0]);
      document.getElementById(buttonId).focus();
    }
  }

  return {
    init: init,
    setDevice: setDevice,
  }
})();
