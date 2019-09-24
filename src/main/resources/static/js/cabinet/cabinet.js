$(document).ready(function() {

  bindProductBtns();
  bindSectionBtns();
  bindCategoryBtns();

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
    sendJsonRequest(
       hostURL + "/cabinet/remove_product/" + productId,
       {},
       function(errorResponse) {},
       function(data) {
         if (!data.succeed) {
            showMessage("ERROR", data.errorMessage);
            return;
         }
         var product = $('[data-product-id=' + productId +']');
         var sectionProducts = $(product).closest('.cabinet_sectionProducts')
         $('[data-product-id=' + productId +']').closest('.product-col').remove();
         if ($(sectionProducts).find('.product').length == 0) {
           $(sectionProducts).addClass('empty');
         }
       },
       hostURL)
}
function removeSection (sectionId) {
    sendJsonRequest(
       hostURL + "/cabinet/remove_section/" + sectionId,
       {},
       function(errorResponse) {},
       function(data) {
         if (!data.succeed) {
            showMessage("ERROR", data.errorMessage);
           return;
         }
         $('.sectionBlock[default-section=true]').find('.cabinet_sectionProducts').append($('[data-section-id=' + sectionId +']').find('.cabinet_sectionProducts').html());
         $('.sectionBlock[default-section=true]').find('.cabinet_sectionProducts').removeClass('empty');
         $('.sectionBlock[data-section-id=' + sectionId +']').remove();
       }
     );
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
      var productId = $(this).closest('.product').attr('data-product-id');
      openConfirmModal(confirmModalDeleteProductMessage + ' ' + productId + '?', removeProduct, productId);
    });
  });


  $('.cabinet_moveToSectionBtn').each(function(index) {
    $(this).click(function(){
      var product= $(this).closest('.product-col');
      var productId = $(this).closest('.product').attr('data-product-id');
      var sectionId = $(this).closest('.product').find('.cabinet_moveToSectionSelect').find('option:selected').attr('data-section-id');
      sendJsonRequest(
        hostURL + '/cabinet/move_product_to_section/' + productId + '/' + sectionId,
        {},
        function(errorRequest) {},
        function(data) {
          if (!data.succeed) {
            showMessage("ERROR", data.errorMessage);
            return;
          }
          var sectionProducts = product.closest('.cabinet_sectionProducts');
          var targetSectionProducts = $('.sectionBlock[data-section-id = "' + sectionId + '"]').find('.cabinet_sectionProducts');
          product.appendTo(targetSectionProducts);
          if (sectionProducts.find('.product').length == 0) {
            sectionProducts.addClass('empty');
          }
          targetSectionProducts.removeClass('empty');
        }
      );
    });
  });

  $('#cabinet_mainCategorySubmitBtn').click(function() {
    var categoryId = $(this).closest('.cabinet_mainCategorySelectBlock').find('option:selected').attr('data-category-id');
    var categoryName = $(this).closest('.cabinet_mainCategorySelectBlock').find('option:selected').val();
    sendJsonRequest (
      hostURL + "/cabinet/set_main_category/" + categoryId,
      {},
      function(errorResponse) {},
      function(data) {
        if (!data.succeed) {
          showMessage("ERROR", data.errorMessage);
          console.log(data.errorMessage);
          return;
        }
        $(".cabinet_mainCategoryName").text(categoryName);
        var categoryMissing = $('.cabinet_categoriesList').find('.cabinet_category[data-category-id = ' + categoryId + ']').length == 0;
        if (categoryMissing) {
          appendCategory(categoryId, categoryName);
        }
      }
    );
  });

  $('#cabinet_addCategorySubmitBtn').click(function() {
    var categoryId = $(this).closest('.cabinet_addCategoryBlock').find('option:selected').attr('data-category-id');
    var categoryName = $(this).closest('.cabinet_addCategoryBlock').find('option:selected').val();
    console.log(categoryId);
    sendJsonRequest (
      hostURL + "/cabinet/add_category/" + categoryId,
      {},
      function(errorResponse) {},
      function(data) {
        if (!data.succeed) {
          showMessage("ERROR", data.errorMessage);
          console.log(data.errorMessage);
          return;
        }
        appendCategory(categoryId, categoryName);
      }
    );
  });

}

function appendCategory(categoryId, categoryName) {
  $(".cabinet_categoriesList").append(
    '<div class="cabinet_category" data-category-id = "' + categoryId + '">' +
      '<div class="cabinet_categoryName">' + categoryName + '</div>' +
      '<button class="cabinet_removeBtn cabinet_categoryRemoveBtn" title="remove">' +
          '<i class="fas fa-times"></i>' +
      '</button>' +
    '</div>'
  );
  bindCategoryBtns();
}

function bindCategoryBtns() {
  $('.cabinet_categoryRemoveBtn').each(function(index) {
    $(this).click(function() {
      var category = $(this).closest('.cabinet_category');
      var categoryId = $(this).closest('.cabinet_category').attr('data-category-id');
      sendJsonRequest(
        hostURL + "/cabinet/remove_category/" + categoryId,
        {},
        function(errorResponse) {},
        function(data) {
          if (!data.succeed) {
             showMessage("ERROR", data.errorMessage);
             return;
          }
          category.remove();
        },
        hostURL
      )
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
