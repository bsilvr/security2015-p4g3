var all_books;
var purchased_books = [];

function get_books(){
    $.ajax({
      type: "GET",
      url: "/books/get_books/",
      dataType: "json",
      success: saveArray,
      error: function (xhr, status, err) {
      }
    });
}

function get_purchases(){
    var email = getCookie("email");
    email = email.replace(/"/g,"");

    $.ajax({
      type: "GET",
      url: "/users/get_purchases/?user=" + email,
      dataType: "json",
      success: savePurchases,
      error: function (xhr, status, err) {
      }
    });
}

function saveArray(array){
    all_books = array;
    processArray();
}

function savePurchases(array){
    purchased_books = [];
    var arr = array;
    for (var i = 0; i < arr.length; i++){
        purchased_books[i] = arr[i].book_id;
    }
    processArray();
}

function purchase_book(){
    var id = event.target.id;

    console.log(id);
    var email = getCookie("email");

    $.ajax({
    url : "/users/buy_book/",
    type: "POST",
    data : {csrfmiddlewaretoken: document.getElementsByName('csrfmiddlewaretoken')[0].value, "email": email, "book_id": id },
    dataType : "json",
});
    document.location.reload(true);

}



function processArray(){
    var out = "";
    var arr = all_books;

    for (var i = 0; i < arr.length; i++){
        out += "<div class=\"col-md-4 portfolio-item\">"+
                    "<a href=\"#\"><img class=\"img-responsive\" src=\""+arr[i].cover+"\"></a>"+
                        "<h3><a href=\"#\">"+arr[i].title+"</a></h3>"+
                        "<p>Author: "+arr[i].author+"<br>"+
                            "Language: "+arr[i].language+"<br><br>";

                            if(purchased_books.contains(arr[i].ebook_id) ){
                                out += "<button class=\"btn btn-default\" value=\""+ arr[i].ebook_id +"\" id=\""+ arr[i].ebook_id +"\">Bought</button></p></div>"+
                                        "</p></div>";
                            }
                            else{
                                out += "<button class=\"btn btn-default\" onClick=\"purchase_book()\" value=\""+ arr[i].ebook_id +"\" id=\""+ arr[i].ebook_id +"\">Buy</button></p></div>"+
                                        "</p></div>";
                            }

    }
    document.getElementById("books").innerHTML = out;
}

// function from: http://stackoverflow.com/questions/1181575/determine-whether-an-array-contains-a-value
Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
}


function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}



// function processArray(){
//     var out = "";
//     var arr = JSON.parse(all_books)
//     var purchased = JSON.parse(purchased_books)
//     var email = getCookie("email");
//
//     console.log(arr);
//     console.log(arr[0]);
//     for (var i = 0; i < arr.length; i++){
//         out += "<div class=\"col-md-4 portfolio-item\">"+
//                     "<a href=\"#\"><img class=\"img-responsive\" src=\""+arr[i].cover+"\"></a>"+
//                         "<h3><a href=\"#\">"+arr[i].title+"</a></h3>"+
//                         "<p>Author: "+arr[i].author+"<br>"+
//                             "Language: "+arr[i].language+"<br><br>"+
//                             "<a class=\"btn btn-default\" href=\"#\">Buy</a></p></div>";
//
//
//                             "<form method=\"post\" action=\"/users/buy_book\">"+
//                                 "<button class=\"btn btn-default\" type=\"submit\">Buy</button>"+
//                                 "<input type=\"hidden\" name=\"book_id\" value=\""+ arr[i].ebook_id +"\">"+
//                                 "<input type=\"hidden\" name=\"email\" value=\""+ email +"\">"+
//                             "</form>"+
//
//                             "</p></div>";
//     }
//     document.getElementById("books").innerHTML = out;
// }


// function processArray(array){
//     var out = "";
//     var arr = JSON.parse(array)
//     console.log(arr);
//     console.log(arr[0]);
//     for (var i = 0; i < arr.length; i++){
//         out += "<div class=\"col-md-4 portfolio-item\">"+
//                     "<a href=\"#\"><img class=\"img-responsive\" src=\""+arr[i].cover+"\"></a>"+
//                         "<h3><a href=\"#\">"+arr[i].title+"</a></h3>"+
//                         "<p>Author: "+arr[i].author+"<br>"+
//                             "Language: "+arr[i].language+"<br><br>"+
//                             "<a class=\"btn btn-default\" href=\"#\">Buy</a></p></div>";
//     }
//     document.getElementById("books").innerHTML = out;
// }
