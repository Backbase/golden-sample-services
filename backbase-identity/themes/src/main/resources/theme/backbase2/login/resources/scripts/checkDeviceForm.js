const checkDeviceController = (function() {
  const identifiers = {
    continueForm: 'bb-check-device-continue-form',
    countdownInfo: 'bb-check-device-countdown-info',
    currentResendTime: 'bb-check-device-resend-time',
    errorModalBody: 'bb-check-device-modal-body',
    errorModalHeader: 'bb-check-device-modal-header',
    lockedResendValidation: 'bb-check-device-resend-locked',
    progressBar: 'bb-check-device-progress-bar',
    resendForm: 'bb-check-device-resend-form',
  };

  const settings = {
    pollingFrequency: 3000,
    countdownInterval: 1000,
    pendingStatus: 'PENDING',
    authActionKey: 'oob-authn-action',
    authPollingValue: 'confirmation-poll',
  };

  let isPolling = false;
  let currentTimeout;
  let currentInterval;
  let targetTime;
  let pageData;

  function isPagePresent() {
    return !!(document.getElementById(identifiers.continueForm));
  }

  function setCountdownTime(time) {
    document.getElementById(identifiers.currentResendTime).innerText = time;
  }

  function hideResendButton(value) {
    document.getElementById(identifiers.resendForm).hidden = value;
    document.getElementById(identifiers.countdownInfo).hidden = !value;
  }

  function setProgressBarValue(value) {
    const progressBar = document.getElementById(identifiers.progressBar);
    const percentage = ((value / pageData.nextResendSeconds) * 100).toFixed(2);
    progressBar.setAttribute('aria-valuenow', value);
    progressBar.style.width = percentage.toString() + '%';
  }

  function getRemainingTime() {
    return Math.round((targetTime - new Date().getTime()) / 1000);
  }

  function updateCountdown() {
    const remainingTime = getRemainingTime();
    setCountdownTime(remainingTime);
    setProgressBarValue(pageData.nextResendSeconds - remainingTime);
    if (remainingTime <= 0) {
      hideResendButton(false);
      return clearInterval(currentInterval);
    }
  }

  function initCountdown() {
    if (!pageData || !pageData.resendAttemptsLeft || !pageData.nextResendSeconds) {
      return hideResendButton(false);
    }

    hideResendButton(true);
    setProgressBarValue(0);
    setCountdownTime(pageData.nextResendSeconds);
    targetTime = new Date().getTime() + pageData.nextResendSeconds * 1000;
    currentInterval = setInterval(updateCountdown, settings.countdownInterval);
  }

  function submitContinueForm() {
    document.getElementById(identifiers.continueForm).submit();
  }

  function updateActionUrls(newUrl) {
    const resendForm = document.getElementById(identifiers.resendForm);
    if (resendForm) {
      resendForm.action = newUrl;
    }

    document.getElementById(identifiers.continueForm).action = newUrl;
    isPolling = false;
  }

  function displayErrorModal() {
    if (modalController) {
      modalController.showModal(identifiers.errorModalHeader, identifiers.errorModalBody);
    }
  }

  function handleSuccessResponse(response) {
    try {
      const result = JSON.parse(response);
      updateActionUrls(result.actionUrl);
  
      if (result.status !== settings.pendingStatus) {
        return submitContinueForm();
      }
      currentTimeout = setTimeout(postCheckDeviceRequest, settings.pollingFrequency);
    } catch(error) {
      displayErrorModal();
    }
  }

  function handleResponse(request) {
    if (currentTimeout) {
      clearTimeout(currentTimeout)
    }

    if (request.status === 200) {
      handleSuccessResponse(request.response);
    } else {
      displayErrorModal();
    }
  }

  function postCheckDeviceRequest() {
    const req = new XMLHttpRequest();
    const actionUrl = document.getElementById(identifiers.continueForm).action;

    req.open("POST", actionUrl);
    req.setRequestHeader('X-Convert-Redirects', 'true');
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.withCredentials = true;
    req.onreadystatechange = function() {
        if (this.readyState === 4) {
          handleResponse(req);
        }
    };

    isPolling = true;
    req.send(encodeURIComponent(settings.authActionKey) + '=' + encodeURIComponent(settings.authPollingValue));
  }

  function init() {
    if (!isPagePresent()) {
      return;
    }
    
    pageData = oobAuthPushCheckDevice;
    postCheckDeviceRequest();
    initCountdown();
  }

  function validateForm() {
    if (getRemainingTime() > 0) {
      return false;
    }

    if (isPolling) {
      document.getElementById(identifiers.lockedResendValidation).hidden = false;
      return false;
    }

    if (currentTimeout) {
      clearTimeout(currentTimeout)
    }
    return true;
  }

  return {
    init: init,
    validateForm: validateForm,
  };
})();