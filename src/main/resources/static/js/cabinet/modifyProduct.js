$(document).ready(function() {
  $("#loadProductImageBtn").click(function(e) {
    e.preventDefault();
    if ($('#loadProductImageInput').get(0).files.length === 0) {
      return;
    }
    $(this).prop('disabled',true);
    var formData = new FormData(document.forms.namedItem("loadProductImageForm"));
    sendImageUploadRequest(formData,
                           function() {
                             console.log("success");
                             location.reload();
                           },
                           function() {},
                           hostURL,
                           "product/" + parseInt(productId, 10)
                          );
  });
});
