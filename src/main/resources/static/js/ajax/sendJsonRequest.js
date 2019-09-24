function sendJsonRequest(url, data, onError, onSuccess) {
  $.ajax({
    headers: {
      'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content'),
    },
    type: "POST",
    contentType: "application/json",
    url: url,
    data: data,
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      console.log(response.responseText);
      if (response.status == 403) {
        location.reload();
      }
      onError(response);
    }
  });
}
