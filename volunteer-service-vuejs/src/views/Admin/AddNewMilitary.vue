<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Генерація токену</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit="registerMilitaryPerson">
          <div class="form-group">
            <label>Ім'я військового</label>
            <input type="text" class="form-control" v-model="firstName" required>
          </div>
          <div class="form-group">
            <label>Прізвище військового</label>
            <input type="text" class="form-control" v-model="lastName" required>
          </div>
          <div class="form-group">
            <label>Військове звання</label>
            <input type="text" class="form-control" v-model="rank" required>
          </div>
          <button type="submit" class="btn btn-primary">Згенерувати</button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
import Swal from "sweetalert2";
import axios from "axios";

export default {
  name: "AddNewMilitary",
  props: ["baseURL"],
  data() {
    return {
      firstName: null,
      lastName: null,
      rank: null,
    };
  },
  methods: {
    async registerMilitaryPerson(e) {
      e.preventDefault();
      // make the post body
      const militaryPerson = {
        firstName: this.firstName,
        lastName: this.lastName,
        rank: this.rank,
      };

      // call the API
      await axios
          .post(`${this.baseURL}user/militarySignup`, militaryPerson)
          .then((response) => {
            // display the token
            Swal.fire({
              title: `Токен для військового успішно згенеровано`,
              html: `Токен: <strong>${response.data.token}</strong>`,
              icon: "success",
              closeOnClickOutside: false,
            }).then(() => {
              Swal.fire({
                title: 'Введіть електронну адресу для надсилання токену',
                input: 'email',
                inputAttributes: {
                  autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Надіслати',
                cancelButtonText: 'Відмінити',
                showLoaderOnConfirm: true,
                inputValidator: (value) => {
                  // Regular expression for basic email validation
                  let re = /\S+@\S+\.\S+/;
                  if (!re.test(value)) {
                    return 'Будь ласка, введіть дійсну електронну адресу';
                  }
                },
                preConfirm: (email) => {
                  return axios.post(`${this.baseURL}user/email`, {
                    email,
                    firstName: this.firstName,
                    lastName: this.lastName,
                    token: response.data.token,
                  }, {
                    headers: {
                      'Authorization': `Bearer ${this.token}`
                    }
                  }).then(response => {
                    if (response.status !== 200) {
                      throw new Error('Не вдалося надіслати електронну пошту')
                    }
                    return email;
                  }).catch(error => {
                    Swal.showValidationMessage(
                        `Запит не вдався: ${error}`
                    )
                  })
                },
                allowOutsideClick: () => !Swal.isLoading()
              }).then((result) => {
                if (result.isConfirmed) {
                  Swal.fire({
                    title: `Токен надіслано на ${result.value}`,
                    icon: "success",
                    closeOnClickOutside: false,
                  });
                }
              })
            });
          })
          .catch((err) => {
            console.log(err);
          });
    },
  },
};
</script>

<!--<script>-->
<!--export default {-->
<!--  data(){-->
<!--    return {-->
<!--      firstName : null,-->
<!--      lastName : null,-->
<!--      rank : null,-->
<!--    }-->
<!--  },-->
<!--  props : ["baseURL", "militaryPersons"],-->
<!--  methods : {-->
<!--    async addMilitaryPerson() {-->
<!--      const newMilitaryPerson = {-->
<!--        firstName : this.firstName,-->
<!--        lastName : this.lastName,-->
<!--        rank : this.rank,-->
<!--      }-->

<!--      await axios({-->
<!--        method: 'post',-->
<!--        url: this.baseURL+"user/militarySignup",-->
<!--        data : JSON.stringify(newMilitaryPerson),-->
<!--        headers: {-->
<!--          'Content-Type': 'application/json'-->
<!--        }-->
<!--      })-->
<!--          .then(res => {-->
<!--            this.$emit("fetchData");-->
<!--            this.$router.push({name:'AdminMilitaryPerson'});-->
<!--            swal({-->
<!--              text: "Military Person Added Successfully! Token: " + res.data.token,-->
<!--              icon: "success",-->
<!--              closeOnClickOutside: false,-->
<!--            });-->
<!--          })-->
<!--          .catch(err => console.log(err));-->
<!--    }-->
<!--  },-->
<!--  mounted(){-->
<!--    if (!localStorage.getItem('token')) {-->
<!--      this.$router.push({name : 'Signin'});-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->


<style scoped>
h4 {
  font-family: 'Roboto', sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
