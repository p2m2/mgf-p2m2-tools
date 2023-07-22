function managePanelDisplay(idDivActive) {
    const allItems = document.getElementsByClassName('menuItem');

    for (var i=0;i<allItems.length; i++) {
        allItems[i].style.display = "none";
    }

    const listTagAmenuItemTop = document.getElementById('onglets').querySelectorAll("a.menuItemTop")
    for (var i=0;i<listTagAmenuItemTop.length; i++) {
        var li = listTagAmenuItemTop[i].parentNode;
        if (listTagAmenuItemTop[i].getAttribute("href") == "#"+idDivActive) {
            li.className="active"
        } else {
            li.classList.remove("active");
        }

    }

    const divId = document.getElementById(idDivActive)
    if (divId) {
        divId.style.display = "block";
    }
}

/** **/
(function () {
document.body.addEventListener('click', function (evt) {
    if (evt.target.className === 'menuItemTop') {
        //console.log(evt.target.getAttribute("href"))
        managePanelDisplay(evt.target.getAttribute("href").substring(1))
    }
}, false);

managePanelDisplay("mgfReader")

})();

/** Manage console output **/

(function () {
    var old = console.log;
    var logger = document.getElementById('log');
    console.error = function (message) {

    if (typeof message == 'object') {
        logger.innerHTML += (JSON && JSON.stringify ? JSON.stringify(message) : message) + '\r\n';
    } else {
        logger.innerHTML += message + '\r\n';
    }
   }
})();

/** Manage Msf Wait */
function waitBlock(bool) {
    if (bool) {
        document.getElementById('boardMgfTools').style.display = "none";
        document.getElementById('msgWait').style.display = "block";
    } else {
        document.getElementById('boardMgfTools').style.display = "block";
        document.getElementById('msgWait').style.display = "none";
    }
}

(function () {
    waitBlock(false);
})();

