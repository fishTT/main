// show only the holdings section
var holdings = document.getElementsByClassName('clsTab1')[0];
if (holdings) {
    document.body.innerHTML = '<div id="mainContent" style="padding: 0 12px 5px; max-width: 750px;"></div>';
    var mainContent = document.getElementById('mainContent');
    mainContent.appendChild(holdings);
}
var links = document.getElementsByTagName("a");
for (var i = 0; i < links.length; i++) {
    links[i].onclick = (e) => e.preventDefault();
}
