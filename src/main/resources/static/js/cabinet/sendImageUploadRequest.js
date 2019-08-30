function sendImageUploadRequest(data, onSuccess, onError, hostURL, type) {
  $.ajax({
    headers: {
      'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content'),
    },
    type: "POST",
    url: hostURL + "/cabinet/upload_image/" + type,
    data: data,

    enctype : 'multipart/form-data',
    contentType : false,
    cache : false,
    processData : false,

    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      console.log(response.responseText);
      onError();
    }
  });
}
