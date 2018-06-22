function getItemHtml(games) {
  return "<li class='list-group-item' style='width: 12em'><span class='badge'>" +
    games.id + "</span>" + games.joinDate + "</li>";

}
function getListHtml(data) {
  return data.map(getItemHtml).join("");
}
function renderList(data) {
  var html = getListHtml(data);
  document.getElementById("games").innerHTML = html;
}
function getData() {
    $.ajax({
        url : '/game',
        type: 'GET',
        success : handleData
    })
}

