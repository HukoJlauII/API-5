let createForm = document.querySelector('#create')
let searchBar = document.querySelector('#search')
let currentLocation = document.location.protocol + "//" + document.location.host;
console.log(currentLocation)
searchBar.getElementsByTagName('button')[0].addEventListener('click', findContact, true)
searchBar.getElementsByTagName('button')[1].addEventListener('click', deleteAllContacts, true)
createForm.getElementsByTagName('button')[0].addEventListener('click', createContact, true)
let tBody = document.querySelector('#tableBody')


function loadContacts() {
    $.ajax({
        url: currentLocation + "/api/contacts",
        type: 'GET',
        contentType: 'application/json',
        success: createAllContacts
    })
}

let createAllContacts = function (data, textStatus, xhr) {

    if (xhr.status === 204) {
        createEmptyRow()
    } else {
        for (let i = 0; i < data.length; i++) {
            createRow(data[i])
        }
    }
}

loadContacts()

function createContact() {
    const contact =
        {
            name: createForm.querySelector('#name').value,
            surname: createForm.querySelector('#surname').value,
            number: createForm.querySelector('#number').value
        }
    $.ajax({
        url: currentLocation + "/api/contacts",
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(contact),
        success: createContactLine
    })

}

function deleteContact(id) {
    const elementToDelete = tBody.querySelector(`#${CSS.escape(id)}`)
    elementToDelete.parentNode.removeChild(elementToDelete)
    $.ajax({
        url: currentLocation + "/api/contacts/" + id,
        type: 'DELETE',
        contentType: 'application/json',
    })
}

function deleteAllContacts() {
    while (tBody.children.length > 1) {
        tBody.removeChild(tBody.lastChild)
    }
    $.ajax({
        url: currentLocation + "/api/contacts",
        type: 'DELETE',
        contentType: 'application/json',
    })
}

function putContact(id) {
    let currentElement = document.querySelector(`#${CSS.escape(id)}`)
    const contact =
        {
            id: id,
            name: currentElement.nextSibling.querySelector('#nameEdit').value,
            surname: currentElement.nextSibling.querySelector('#surnameEdit').value,
            number: currentElement.nextSibling.querySelector('#numberEdit').value
        }

    currentElement.querySelector('.name').innerHTML = contact.name
    currentElement.querySelector('.surname').innerHTML = contact.surname
    currentElement.querySelector('.number').innerHTML = contact.number
    $.ajax({
        url: currentLocation + "/api/contacts/" + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(contact),
    })
    currentElement.style.display = 'table-row'
    currentElement.parentNode.removeChild(currentElement.nextSibling)
}

function findContact() {

    let id = searchBar.querySelector('.form-control').value
    while (tBody.children.length > 1) {
        tBody.removeChild(tBody.lastChild)
    }
    if (id === '') {
        loadContacts()
    }
    if (/\d+$/.test(id)) {
        $.ajax({
            url: currentLocation + "/api/contacts/" + id,
            type: 'GET',
            contentType: 'application/json',
            success: createContactLine
        })
    }
}


let createContactLine = function (data, textStatus, xhr) {
    if (xhr.status === 204) {
        createEmptyRow()
    } else {
        createRow(data)
    }
}

function createEmptyRow() {
    const row = document.createElement('tr')
    row.classList.add(`text-center`)
    const text = document.createElement('td')
    text.colSpan = 4
    text.innerHTML = "Контакта с таким id не существует"
    row.appendChild(text)
    tBody.appendChild(row)
}

function createRow(data) {
    console.log(data)
    const row = document.createElement('tr')
    row.id = data.id
    row.classList.add(`text-center`)
    const name = document.createElement('td')
    name.classList.add('name')
    name.innerHTML = data.name
    const surname = document.createElement('td')
    surname.innerHTML = data.surname
    surname.classList.add('surname')
    const number = document.createElement('td')
    number.classList.add('number')
    number.innerHTML = data.number
    row.appendChild(name)
    row.appendChild(surname)
    row.appendChild(number)
    row.innerHTML += ("<td class=\"status\"><span class=\"waiting\">Изменить</span></td>\n" +
        "                            <td>\n" +
        "                                <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "                                    <span aria-hidden=\"true\"><i class=\"fa fa-close\"></i></span>\n" +
        "                                </button>\n" +
        "                            </td>")
    const editButton = row.getElementsByTagName('span')[0]
    editButton.addEventListener('click', function () {
        showEditForm(data.id)
    }, true)
    const deleteButton = row.getElementsByTagName('button')[0]
    deleteButton.addEventListener('click', function () {
        deleteContact(data.id)
    })
    tBody.appendChild(row)
}

function showEditForm(id) {
    const currentElement = document.querySelector(`#${CSS.escape(id)}`)
    currentElement.style.display = 'none'
    const editForm = document.createElement('tr')
    editForm.classList.add(`text-center`)
    editForm.innerHTML = '<td class="d-flex align-items-center">\n' +
        '                                    <input class="form-control" type="text"  id="nameEdit" placeholder="Введите имя" value="' + currentElement.querySelector('.name').textContent + '" required>\n' +
        '                                </td>\n' +
        '                                <td class="">\n' +
        '                                    <input class="form-control" type="text" id="surnameEdit" placeholder="Введите фамилию" value="' + currentElement.querySelector('.surname').textContent + '" required>\n' +
        '                                </td>\n' +
        '                                <td><input class="form-control" type="text" id="numberEdit" placeholder="Введите номер" value="' + currentElement.querySelector('.number').textContent + '" required ></td>\n' +
        '                                <td class="status"><span class="danger">Отмена</span></td>\n' +
        '                                <td>\n' +
        '                                    <button type="submit" class="close">\n' +
        '                                        <span aria-hidden="true"><i class="fa fa-check"></i></span>\n' +
        '                                    </button>\n' +
        '                                </td>';
    let submitButton = editForm.getElementsByTagName('span')[1]
    let closeButton = editForm.getElementsByTagName('span')[0]
    submitButton.addEventListener('click', function () {
        putContact(id)
    }, true)
    closeButton.addEventListener('click', closeEditForm, true)

    currentElement.parentNode.insertBefore(editForm, currentElement.nextSibling)
}

function closeEditForm(e) {
    const button = e.currentTarget
    let editForm = button.parentNode.parentNode
    let currentRow = editForm.previousSibling
    currentRow.style.display = 'table-row'
    editForm.parentNode.removeChild(editForm)
}



