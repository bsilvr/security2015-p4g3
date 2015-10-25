

function get_books(){
    $.ajax({
      type: "GET",
      url: "http://127.0.0.1:8000/books/get_books/",
      dataType: "json",
      success: processArray,
      error: function (xhr, status, err) {
      }
    });
}


function processArray(array){
    var out = "";
    var arr = JSON.parse(array)
    console.log(arr);
    console.log(arr[0]);
    for (var i = 0; i < arr.length; i++){
        out += "<div class=\"col-md-4 portfolio-item\">"+
                    "<a href=\"#\"><img class=\"img-responsive\" src=\""+arr[i].cover+"\"></a>"+
                        "<h3><a href=\"#\">"+arr[i].title+"</a></h3>"+
                        "<p>Author: "+arr[i].author+"<br>"+
                            "Language: "+arr[i].language+"<br><br>"+
                            "<a class=\"btn btn-default\" href=\"#\">Buy</a></p></div>";
    }
    document.getElementById("books").innerHTML = out;
}
