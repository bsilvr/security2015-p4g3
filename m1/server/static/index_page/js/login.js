
$(document).ready(function(){
  $('#login-trigger').click(function(){
    $(this).next('#login-content').slideToggle();
    $(this).toggleClass('active');

    if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
      else $(this).find('span').html('&#x25BC;')
    })
});


function checkCookie() {
    var username=getCookie("fname");
    var su = document.getElementById('logedIn');
    var sm = document.getElementById('notLogedIn');
    if (username!="") {
        var str = document.getElementById('username');
        str.innerHTML = "Welcome " + username;
        sm.style.display = 'none';
        su.style.display = 'block';
    }else{
        su.style.display = 'none';
        sm.style.display = 'block';
    }
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

function logout(){
    document.cookie = "fname=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    document.cookie = "email=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    checkCookie()

}
