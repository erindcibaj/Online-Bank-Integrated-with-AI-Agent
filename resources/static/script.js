function togglePasswordVisibility(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (input.type === "password") {
        input.type = "text";
        icon.style.color = "#262626";
    } else {
        input.type = "password";
        icon.style.color = "gray";
    }
}

// Usage
function see() {
    togglePasswordVisibility("password", "see");
}

function seeConfirmPassword() {
    togglePasswordVisibility("confirmPassword", "seeConfirmPassword");
}

function seeOldPassword() {
    togglePasswordVisibility("oldPassword", "seeOldPassword");
}



function validateForm() {
    var check0 = document.getElementById("check0").style.color === "green";
    var check1 = document.getElementById("check1").style.color === "green";
    var check2 = document.getElementById("check2").style.color === "green";
    var check3 = document.getElementById("check3").style.color === "green";
    var check4 = document.getElementById("check4").style.color === "green";

    // Enable button only if all conditions are true
    document.getElementById("submitButton").disabled = !(check0 && check1 && check2 && check3 && check4);
}

function check() {
    var password = document.getElementById("password").value;
    var check3 = document.getElementById("check3");
    
        if(password === "") {
        check0.style.color = "red";   
        check1.style.color = "red";
        check2.style.color = "red";
        check3.style.color = "red";
        check4.style.color = "red";
        
        return;
    }
    
    var password = document.getElementById("password").value;

    // Validate length (8 to 16 characters)
    document.getElementById("check0").style.color = (password.length >= 8 && password.length <= 16) ? "green" : "red";

    // Validate numerical character
    document.getElementById("check1").style.color = /\d/.test(password) ? "green" : "red";

    // Validate uppercase and lowercase letters
    document.getElementById("check2").style.color =  /[a-z]/.test(password) && /[A-Z]/.test(password) ? "green" : "red";

    // Validate no spaces
    document.getElementById("check3").style.color = !/\s/.test(password) ? "green" : "red";
    
    document.getElementById("check4").style.color = /[!@#$%^&*(),.?":{}|<>]/.test(password) ? "green" : "red";

    
    validateForm();
}


    
    function validateAmount(input) {
        if (input.value < 0) {
            input.setCustomValidity("Amount cannot be negative.");
        } else if (input.value == 0) {
            input.setCustomValidity("Amount cannot be zero.");
        } else {
            input.setCustomValidity("");
        }
    }
    

function filterToAccount() {
        const fromSelect = document.getElementById("fromAccountNumber");
        const toSelect = document.getElementById("toAccountNumber");
        const selectedFrom = fromSelect.value;

        // Reset all options
        for (let option of toSelect.options) {
            option.hidden = false;
        }

        // Hide the selected "from" account in the "to" dropdown
        for (let option of toSelect.options) {
            if (option.value === selectedFrom && option.value !== "") {
                option.hidden = true;
                // Optional: Reset if currently selected
                if (toSelect.value === option.value) {
                    toSelect.value = "";
                }
                break;
            }
        }
    }

    function filterFromAccount() {
        const fromSelect = document.getElementById("fromAccountNumber");
        const toSelect = document.getElementById("toAccountNumber");
        const selectedTo = toSelect.value;

        // Reset all options
        for (let option of fromSelect.options) {
            option.hidden = false;
        }

        // Hide the selected "to" account in the "from" dropdown
        for (let option of fromSelect.options) {
            if (option.value === selectedTo && option.value !== "") {
                option.hidden = true;
                // Optional: Reset if currently selected
                if (fromSelect.value === option.value) {
                    fromSelect.value = "";
                }
                break;
            }
        }
        
    }
    
    function formatCardNumber(input) {
    // Remove all non-digit characters
    let value = input.value.replace(/\D/g, '');

    // Group every 4 digits and join them with a space
    let formattedValue = value.match(/.{1,4}/g)?.join(' ') || '';

    // Set the formatted value back to the input
    input.value = formattedValue;
}



