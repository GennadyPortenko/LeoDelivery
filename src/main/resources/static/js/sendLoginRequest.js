function sendLoginRequest(requestData, onSuccess, onError, hostURL) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/login/otp/ajax/",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      onError();
    }
  });
}
