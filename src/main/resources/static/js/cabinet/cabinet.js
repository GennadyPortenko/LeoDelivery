$(document).ready(function() {

  bindProductBtns();
  bindSectionBtns();

  $("#loadPartnerImageBtn").click(function(e) {
    e.preventDefault();
    if ($('#loadPartnerImageInput').get(0).files.length === 0) {
      return;
    }
    $(this).prop('disabled',true);
    var formData = new FormData(document.forms.namedItem("loadPartnerImageForm"));
    sendImageUploadRequest(formData,
                           function() {
                             console.log("success");
                             location.reload();
                           },
                           function() {},
                           hostURL,
                           "main"
                          );
  });
});

function removeProduct(productId) {
    sendRemoveProductRequest(productId,
       function() {
         var product = $('[data-product-id=' + productId +']');
         var sectionProducts = $(product).parent()
         $('[data-product-id=' + productId +']').remove();
         console.log($(sectionProducts).find('.product'));
         if ($(sectionProducts).find('.product').length == 0) {
           $(sectionProducts).find('.cabinet_empty').removeClass('hidden');
         }
       },
       function() {},
       hostURL)
}
function removeSection (sectionId) {
    sendRemoveSectionRequest(sectionId,
       function() {
         $('[data-section-id=' + sectionId +']').find('.cabinet_empty').remove();
         $('[default-section=true]').find('.cabinet_sectionProducts').append($('[data-section-id=' + sectionId +']').find('.cabinet_sectionProducts').html());
         $('[data-section-id=' + sectionId +']').remove();
       },
       function() {},
       hostURL);
}

function bindSectionBtns() {
  $('.cabinet_removeSectionBtn').each(function(index) {
    $(this).click(function() {
      var sectionId = $(this).parent().attr('data-section-id');
      openConfirmModal(confirmModalDeleteSectionMessage + ' ' + sectionId + '?', removeSection, sectionId);
    });
  });
}

function bindProductBtns() {
  $('.cabinet_removeProductBtn').each(function(index) {
    $(this).click(function() {
      var productId = $(this).parent().attr('data-product-id');
      openConfirmModal(confirmModalDeleteProductMessage + ' ' + productId + '?', removeProduct, productId);
    });
  });
}

function openConfirmModal(message, action, target) {
  $('#cabinetConfirmModal').find('.message').text(message);
  $('#cabinetConfirmModal').find('.yesBtn').off('click');
  $('#cabinetConfirmModal').find('.yesBtn').click(function() {
    action(target);
    $('#cabinetConfirmModal').modal('hide');
  });
  $('#cabinetConfirmModal').modal('show');
}
