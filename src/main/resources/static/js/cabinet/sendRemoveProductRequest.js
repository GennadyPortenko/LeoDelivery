function sendRemoveProductRequest(productId, onSuccess, onError, hostURL) {
  $.ajax({
    headers: {
      'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content'),
    },
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/contractor/cabinet/remove_product/" + productId,
    data: {},
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
