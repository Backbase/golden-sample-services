function loadBackground() {
  const numberOfBackgroundsAvailable = 3;
  document.body.classList.add('identity-page--background-' + Math.ceil(Math.random() * numberOfBackgroundsAvailable));
  document.body.classList.add('identity-page--ready');
}
