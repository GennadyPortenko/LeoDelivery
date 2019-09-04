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
                             location.reload();
                           },
                           function() {},
                           hostURL,
                           "main"
                          );
  });
  $("#loadPartnerLogoBtn").click(function(e) {
    e.preventDefault();
    if ($('#loadPartnerLogoInput').get(0).files.length === 0) {
      return;
    }
    $(this).prop('disabled',true);
    var formData = new FormData(document.forms.namedItem("loadPartnerLogoForm"));
    sendImageUploadRequest(formData,
                           function() {
                             location.reload();
                           },
                           function() {},
                           hostURL,
                           "logo"
                          );
  });
});

function removeProduct(productId) {
    sendRemoveProductRequest(productId,
       function() {
         var product = $('[data-product-id=' + productId +']');
         var sectionProducts = $(product).parent()
         $('[data-product-id=' + productId +']').remove();
         if ($(sectionProducts).find('.product').length == 0) {
           $(sectionProducts).addClass('empty');
         }
       },
       function() {},
       hostURL)
}
function removeSection (sectionId) {
    sendRemoveSectionRequest(sectionId,
       function() {
         $('.sectionBlock[default-section=true]').find('.cabinet_sectionProducts').append($('[data-section-id=' + sectionId +']').find('.cabinet_sectionProducts').html());
         $('.sectionBlock[default-section=true]').find('.cabinet_sectionProducts').removeClass('empty');
         $('.sectionBlock[data-section-id=' + sectionId +']').remove();
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
  $('.cabinet_moveToSectionBtn').each(function(index) {
    $(this).click(function(){
      var product= $(this).closest('.product-col');
      sendMoveProductToSectionRequest(
        $(this).closest('.product').attr('data-product-id'),
        $(this).closest('.product').find('.cabinet_moveToSectionSelect').find('option:selected').attr('data-section-id'),
        function(data) {
          if (!data.succeed) {
            $('#cabinetMessageModal').find('.message').addClass('errorMessage');
            $('#cabinetMessageModal').find('.message').text(data.errorMessage);
            $('#cabinetMessageModal').modal('show');
          } else {
            var sectionProducts = product.closest('.cabinet_sectionProducts');
            var targetSectionProducts = $('.sectionBlock[data-section-id = "' + data.sectionId + '"]').find('.cabinet_sectionProducts');
            product.appendTo(targetSectionProducts);
            if (sectionProducts.find('.product').length == 0) {
              sectionProducts.addClass('empty');
            }
            targetSectionProducts.removeClass('empty');
          }
        },
        function() {  },
        hostURL
      );
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
