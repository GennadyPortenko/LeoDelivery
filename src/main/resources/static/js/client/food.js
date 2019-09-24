$(document).ready(function() {
  bindCategoryBtns();
  bindApplyFiltersBtn();
});

function bindCategoryBtns() {
  $('.food_category').click(function() {
    $(this).toggleClass('selected');
  });
}

function bindApplyFiltersBtn() {
  $('.food_applyFiltersBtn').click(function() {
    var locationHref = hostURL + "/food";
    if ($('.food_category.selected').length > 0) {
      locationHref += '?'
    }

    var selectedCategories = '';
    $('.food_category.selected').each(function(index) {
      selectedCategories += $(this).text() + ',';
    });
    if (selectedCategories !== '') {
      selectedCategories = selectedCategories.slice(0, selectedCategories.length - 1);
      locationHref += 'categories=' + selectedCategories;
    }

    location.href = locationHref;
  });
}
