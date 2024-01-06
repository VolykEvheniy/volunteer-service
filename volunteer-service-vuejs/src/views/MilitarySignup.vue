<template>
  <div class="container">
    <!--    Logo Div-->
    <div class="row">
      <div class="col-12 text-center pt-3">
        <router-link :to="{ name: 'Home' }">
          <img id="logo" src="../assets/icon.png" />
        </router-link>
      </div>
    </div>

    <div class="row">
      <div class="col-12 justify-content-center d-flex flex-row pt-5">
        <div id="signup-div" class="flex-item border">
          <h2 class="pt-4 pl-4">Register Military Person</h2>
          <form @submit="registerMilitaryPerson" class="pt-4 pl-4 pr-4">
            <div class="form-row">
              <div class="col">
                <div class="form-group">
                  <label>First Name</label>
                  <input
                      type="name"
                      class="form-control"
                      v-model="firstName"
                      required
                  />
                </div>
              </div>
              <div class="col">
                <div class="form-group">
                  <label>Last Name</label>
                  <input
                      type="name"
                      class="form-control"
                      v-model="lastName"
                      required
                  />
                </div>
              </div>
            </div>
            <div class="form-group">
              <label>Rank</label>
              <input
                  type="text"
                  class="form-control"
                  v-model="rank"
                  required
              />
            </div>
            <button type="submit" class="btn btn-primary mt-2 py-0">
              Register Military Person
            </button>
          </form>
          <hr />
          <small class="form-text text-muted pt-2 pl-4 text-center"
          >Already Registered?</small
          >
          <p class="text-center">
            <router-link
                class="btn btn-dark text-center mx-auto px-5 py-1 mb-2"
                :to="{ name: 'Signin' }"
            >Signin Here</router-link
            >
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MilitarySignup",
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
            swal({
              text: `Military person registered successfully. Token: ${response.data.token}`,
              icon: "success",
              closeOnClickOutside: false,
            });
          })
          .catch((err) => {
            console.log(err);
          });
    },
  },
};
</script>

<!-- Copy the style from your Signup.vue -->
<style scoped>
.btn-dark {
  background-color: #e7e9ec;
  color: #000;
  font-size: smaller;
  border-radius: 0;
  border-color: #adb1b8 #a2a6ac #a2a6ac;
}

.btn-primary {
  background-color: #f0c14b;
  color: black;
  border-color: #a88734 #9c7e31 #846a29;
  border-radius: 0;
}

#logo {
  width: 150px;
}

@media only screen and (min-width: 992px) {
  #signup-div {
    width: 40%;
  }
}
</style>
