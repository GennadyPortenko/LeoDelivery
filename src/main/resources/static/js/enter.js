$(document).ready(function() {
  var loginForm = $("#loginForm");
  var loginPhoneInput = $('#loginPhoneInput');
  var loginPasswordInput = $('#loginPasswordInput');
  var phoneMask = '+7 (999) 999-9999';
  var passwordMask = '999999';

  var currentPhone;

  loginPhoneInput.val('');
  loginPasswordInput.val('');

  loginPhoneInput.focus(function() { loginPhoneInput.val('') });
  loginPasswordInput.focus(function() { loginPasswordInput.val('') });

  loginPasswordInput.mask(passwordMask, { autoclear : false , completed : function() {
    if (currentPhone == undefined) {
      return;
    }
    var codeValue = loginPasswordInput.val();
    console.log('sending an OTP ajax request for phone ' + currentPhone + ' with code ' + codeValue );
    var loginRequest = {};
    loginRequest['phone'] = currentPhone;
    loginRequest['otp'] = codeValue;
    sendLoginRequest(loginRequest,
                     function(response) {
                       var authenticated = response.authenticated;
                       if (authenticated) {
                         processAjaxSuccessAuth(response.phone);
                       } else {
                         processAjaxFailedAuth();
                       }
                     },
                     function() { console.log('error') },
                     hostURL
    );
  }});

  loginPhoneInput.mask(phoneMask, { autoclear : false, completed : function() {
    currentPhone = loginPhoneInput.val();
    console.log('sending an OTP request for phone ' + currentPhone);
    var loginTimer = $('#loginTimer');
    sendOTPRequest(currentPhone,
                   function(response) {
                     if (response.sent) {
                       loginPasswordInput.focus();
                       loginTimer.countdown({
	                     'seconds': 60,
                         'on-stop' : function() {
                                       loginTimer.addClass();
                                     }
	                   });
	                   loginPasswordInput.removeClass('hidden');
                     } else {

                     }
                   },
                   function() { console.log('error') },
                   hostURL
    );
  }});

});

function processAjaxSuccessAuth(phone) {
  console.log('processing');
  $('.topbarEnter').addClass('hidden');
  $('.topbarLogoutForm').removeClass('hidden');
  $('.topbarUsername').text(phone);
  $('.topbarUsername').removeClass('hidden');
  $('#personLoginModal').modal('hide');
}
function processAjaxFailedAuth() {
  $('.loginWrongOtp').removeClass('hidden');
}
