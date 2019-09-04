function sendMoveProductToSectionRequest(productId, sectionId, onSuccess, onError, hostURL) {
  $.ajax({
    headers: {
      'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content'),
    },
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/cabinet/move_product_to_section/" + productId,
    data: JSON.stringify( { sectionId : parseInt(sectionId, 10) } ),
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      console.log(response.responseText);
      onError();
    }
  });
}
