package com.example.myapp010bhadejcislo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp010bhadejcislo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val deck = mutableListOf<Card>()
    private val playerHand = mutableListOf<Card>()
    private val dealerHand = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNewGame.setOnClickListener { startNewGame() }
        binding.btnHit.setOnClickListener { playerHits() }
        binding.btnStand.setOnClickListener { playerStands() }

        startNewGame()
    }

    private fun startNewGame() {
        deck.clear()
        playerHand.clear()
        dealerHand.clear()

        buildDeck()
        deck.shuffle()

        playerHand.add(drawCard())
        dealerHand.add(drawCard())
        playerHand.add(drawCard())
        dealerHand.add(drawCard())

        updateUI()
        binding.tvResult.text = ""
        binding.btnHit.isEnabled = true
        binding.btnStand.isEnabled = true
    }

    private fun buildDeck() {
        val suits = listOf("♠", "♥", "♦", "♣")
        val ranks = listOf("2","3","4","5","6","7","8","9","10","J","Q","K","A")
        for (suit in suits) {
            for (rank in ranks) {
                deck.add(Card(rank, suit))
            }
        }
    }

    private fun drawCard(): Card {
        return deck.removeAt(0)
    }

    private fun playerHits() {
        playerHand.add(drawCard())
        updateUI()

        val playerScore = calculateScore(playerHand)
        if (playerScore > 21) {
            binding.tvResult.text = "Bust! You lose."
            binding.btnHit.isEnabled = false
            binding.btnStand.isEnabled = false
        }
    }

    private fun playerStands() {
        binding.btnHit.isEnabled = false
        binding.btnStand.isEnabled = false

        // Dealer plays
        dealerPlay()
    }

    private fun dealerPlay() {
        while (calculateScore(dealerHand) < 17) {
            dealerHand.add(drawCard())
            updateUI()
        }

        val dealerScore = calculateScore(dealerHand)
        val playerScore = calculateScore(playerHand)

        when {
            dealerScore > 21 -> binding.tvResult.text = "Dealer busts! You win!"
            dealerScore > playerScore -> binding.tvResult.text = "Dealer wins with $dealerScore."
            dealerScore < playerScore -> binding.tvResult.text = "You win with $playerScore!"
            else -> binding.tvResult.text = "It's a tie at $playerScore."
        }
    }

    private fun calculateScore(hand: List<Card>): Int {
        var total = 0
        var aces = 0

        for (card in hand) {
            total += when (card.rank) {
                "A" -> {
                    aces++
                    11
                }
                "K", "Q", "J" -> 10
                else -> card.rank.toInt()
            }
        }

        // Adjust for Aces if total > 21
        while (total > 21 && aces > 0) {
            total -= 10
            aces--
        }

        return total
    }

    private fun updateUI() {
        binding.tvPlayerHand.text = "Player: ${handToString(playerHand)} (Score: ${calculateScore(playerHand)})"
        binding.tvDealerHand.text = "Dealer: ${handToString(dealerHand)} (Score: ${calculateScore(dealerHand)})"
    }

    private fun handToString(hand: List<Card>): String {
        return hand.joinToString(" ") { "${it.rank}${it.suit}" }
    }
}
