const dropdownController = (function() {
  function createDropdownHandler(menuSelector, buttonSelector, addCloseMenuEvent) {
    function targetIsNotLocaleOperation(target) {
      return menu.classList.contains('show') &&
        target !== menu &&
        target.parentNode !== menu;
    }
  
    const menu = document.getElementById(menuSelector);
    const button = document.getElementById(buttonSelector);
  
    if (button) {
      // Open and close on button click
      button.addEventListener('click', function(event) {
        menu.classList.toggle('show');
        event.stopPropagation();
      });
  
      // Close on click outside of locale menu
      window.addEventListener('click', function(event) {
        if (targetIsNotLocaleOperation(event.target)) {
          menu.classList.remove('show');
        }
      });
  
      if (addCloseMenuEvent) {
        menu.addEventListener('click', function(event) {
          menu.classList.remove('show');
        });
        menu.addEventListener('keyup', function(event) {
          if ((event.key === 'Enter' || event.key === ' ')) {
            menu.classList.remove('show');
          }
        });
      }
    }
  }

  return {
    createDropdownHandler: createDropdownHandler,
  }
})()
