$(document).ready(function() {
  var loginForm = $("#loginForm");
  var loginPhoneInput = $('#loginPhoneInput');
  var loginPasswordInput = $('#loginPasswordInput');
  var phoneMask = '+7 (999) 999-9999';
  var passwordMask = '999999';

  $("#loginSubmitBtn").click(function(){
    console.log("aa");
    $.each(loginForm.elements, function(k, elem) {
      console.log(elem)
    });
    // loginForm.submit();
  });

  loginPasswordInput.mask(passwordMask);

  loginPhoneInput.mask(phoneMask, { autoclear : false, completed : function() {
    var phoneValue = loginPhoneInput.val();
    console.log('sending an OTP request for phone ' + phoneValue);
    sendOTPRequest(phoneValue,
      function(data) {
        if (data.sent) {

        }
      },
      function() { console.log('error') },
      hostURL);
  }});

});