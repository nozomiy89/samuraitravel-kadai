const stripe = Stripe('pk_test_51OWDkxKHuRqrvQg33FzgE3TsvhIbqFDv6ehV2Rx27XxWsFF8Grqkjt9hLysIcdq9JsuQjcR5vzC5C8MkHYAYWsrC00tSiix5dP');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});