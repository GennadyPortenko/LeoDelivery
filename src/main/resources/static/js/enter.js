$(document).ready(function() {
  var loginForm = $("#loginForm");
  var loginPhoneInput = $('#loginPhoneInput');
  var loginPasswordInput = $('#loginPasswordInput');
  var phoneMask = '+7 (999) 999-9999';
  var passwordMask = '999999';

  loginPhoneInput.focus(function() { loginPhoneInput.val('') });
  loginPasswordInput.focus(function() { loginPasswordInput.val('') });

  loginPasswordInput.mask(passwordMask, { autoclear : false });

  loginPhoneInput.mask(phoneMask, { autoclear : false, completed : function() {
    var phoneValue = loginPhoneInput.val();
    console.log('sending an OTP request for phone ' + phoneValue);
    sendOTPRequest(phoneValue,
      function(response) {
        if (response.sent) {
          loginPhoneInput.prop('disabled', true);
          $('#loginTimer').countdown({
	        'seconds': 60,
            'on-stop' : function() {
                          loginPhoneInput.prop('disabled', false);
                        }
	      });
        }
      },
      function() { console.log('error') },
      hostURL);
  }});

});