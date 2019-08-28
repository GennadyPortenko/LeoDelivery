function sendOTPRequest(phone , onSuccess, onError, hostURL) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/login/otp/" + phone,
    data: {},
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      console.log(JSON.parse(response.responseText));
      onError();
    }
  });
}
