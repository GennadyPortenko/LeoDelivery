$(document).ready(function() {
  $('.chooseCityBtn').click(function() {
    $('.chooseLocation_city').addClass('hidden');;
    $(this).parent().addClass('wide');
    $(this).parent().removeClass('hidden');
    $(this).parent().find('.chooseCityBtn').addClass('hidden');
    $(this).parent().find('.chooseLocation_district').removeClass('hidden');
    $('#chooseLocation_back').removeClass('hidden');
   $('.chooseDistrictMessage').addClass('hidden');
   $('.chooseDistrictMessage').removeClass('hidden');
   $('.chooseCityMessage').addClass('hidden');
  });
  $('#chooseLocation_back').click(function() {
    $('.chooseLocation_city').removeClass('wide');;
    $('.chooseLocation_city').removeClass('hidden');;
    $('.chooseCityBtn').removeClass('hidden');;
    $('.chooseLocation_district').addClass('hidden');;
    $(this).addClass('hidden');
   $('.chooseDistrictMessage').addClass('hidden');
   $('.chooseCityMessage').removeClass('hidden');
  });
});

