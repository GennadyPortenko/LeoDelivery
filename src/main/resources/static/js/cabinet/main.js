$(document).ready(function() {
  bindRemoveBtns();
});

function bindRemoveBtns() {
  $('.cabinet_removeSectionBtn').each(function(index) {
    $(this).click(function() {
      var sectionId = $(this).parent().attr('data-section-id');
      sendRemoveSectionRequest(parseInt(sectionId, 10),
                               function() {
                                 console.log($(this).parent());
                                 removeSection(sectionId);
                               },
                               function() {},
                               hostURL);
    });
  });
}

function removeSection(id) {
  $('[data-section-id=' + id +']').remove();
}
