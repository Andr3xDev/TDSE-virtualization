function getGreeting() {
    let nameVar = document.getElementById("textInputGet").value;
    let url = "/api/greeting?name=" + encodeURIComponent(nameVar);

    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            document.getElementById("getGreeting").innerHTML = data.response;
        })
}

function getHello() {
    let url = "/api/hello";

    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            document.getElementById("getHello").innerHTML = data.response;
        })
}

function getColors() {
    let url = "/api/colors";

    fetch(url, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
        })
        .then(data => {
            const colorsArray = JSON.parse(data.response);
            document.getElementById("getColors").innerHTML = "Current Palette:<br>" + colorsArray.join('<br>');
        })
}

function postColor() {
    let colorValue = document.getElementById("textInputPost").value;
    let url = "/api/colors?color=" + encodeURIComponent(colorValue);

    fetch(url, { method: 'POST' })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
        })
        .then(data => {
            const colorsArray = JSON.parse(data.response);
            document.getElementById("postColor").innerHTML = "Updated Palette:<br>" + colorsArray.join('<br>');
        })
}