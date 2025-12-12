package org.lld.practice.design_wallet_system.improved_solution.services;

import org.lld.practice.design_wallet_system.improved_solution.exceptions.WalletNotFoundException;
import org.lld.practice.design_wallet_system.improved_solution.models.Wallet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing wallets.
 */
public class WalletService {
    
    private final Map<String, Wallet> walletsByUserId = new ConcurrentHashMap<>();
    private final Map<String, Wallet> walletsById = new ConcurrentHashMap<>();

    /**
     * Create a new wallet for a user.
     */
    public Wallet createWallet(String userId) {
        if (walletsByUserId.containsKey(userId)) {
            throw new IllegalStateException("User already has a wallet: " + userId);
        }
        
        Wallet wallet = new Wallet(userId);
        walletsByUserId.put(userId, wallet);
        walletsById.put(wallet.getWalletId(), wallet);
        
        System.out.printf("💳 Created wallet %s for user %s%n", wallet.getWalletId(), userId);
        return wallet;
    }

    /**
     * Get wallet by wallet ID.
     */
    public Optional<Wallet> getWallet(String walletId) {
        return Optional.ofNullable(walletsById.get(walletId));
    }

    /**
     * Get wallet by wallet ID, throwing exception if not found.
     */
    public Wallet getWalletOrThrow(String walletId) {
        return getWallet(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    /**
     * Get wallet by user ID.
     */
    public Optional<Wallet> getWalletByUserId(String userId) {
        return Optional.ofNullable(walletsByUserId.get(userId));
    }

    /**
     * Get wallet by user ID, throwing exception if not found.
     */
    public Wallet getWalletByUserIdOrThrow(String userId) {
        return getWalletByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("User wallet not found: " + userId));
    }

    /**
     * Get all wallets.
     */
    public Collection<Wallet> getAllWallets() {
        return Collections.unmodifiableCollection(walletsById.values());
    }

    /**
     * Check if user has a wallet.
     */
    public boolean hasWallet(String userId) {
        return walletsByUserId.containsKey(userId);
    }
}

