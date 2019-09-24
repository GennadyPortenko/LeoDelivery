function showMessage(type, message) {
  switch(type) {
    case "ERROR" :
      $('#messageModal').find('.message').addClass('errorMessage');
      break;
    default :
      $('#messageModal').find('.message').removeClass('errorMessage');
  }
  $('#messageModal').find('.message').text(message);
  $('#messageModal').modal('show');
}
