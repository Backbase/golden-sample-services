const modalController = (function() {
  const modalId = 'bb-modal';
  const headerId = 'bb-modal-header';
  const bodyId = 'bb-modal-body';

  function hideModal() {
    document.getElementById(headerId).innerHTML = '';
    document.getElementById(bodyId).innerHTML = '';
    document.getElementById(modalId).hidden = true;
  }

  function init() {
    hideModal();
  }

  function showModal(headerImplantId, bodyImplantId) {
    document.getElementById(headerId).innerHTML = document.getElementById(headerImplantId).innerHTML;
    document.getElementById(bodyId).innerHTML = document.getElementById(bodyImplantId).innerHTML;
    document.getElementById(modalId).hidden = false;
  }

  return {
    init: init,
    showModal: showModal,
  }
})()