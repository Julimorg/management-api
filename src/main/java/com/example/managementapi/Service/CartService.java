package com.example.managementapi.Service;


import com.example.managementapi.Dto.Request.Cart.AddItemToCartReq;
import com.example.managementapi.Dto.Request.Cart.ProductQuantityReq;
import com.example.managementapi.Dto.Response.Cart.CartItemDetailRes;
import com.example.managementapi.Dto.Response.Cart.GetCartRes;
import com.example.managementapi.Dto.Response.Product.ProductForCartItem;
import com.example.managementapi.Entity.Cart;
import com.example.managementapi.Entity.CartItem;
import com.example.managementapi.Entity.Product;
import com.example.managementapi.Entity.User;
import com.example.managementapi.Mapper.CartMapper;
import com.example.managementapi.Repository.CartRepository;
import com.example.managementapi.Repository.ProductRepository;
import com.example.managementapi.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final ProductRepository productRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_STAFF' , 'ROLE_ADMIN')")
    public GetCartRes addProductToCart(String userId, AddItemToCartReq request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //? Check Cart đã có chưa -> Nếu chưa thì tạo Cart
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            log.info("Đang tạo Cart");
            Cart newCart = Cart.builder()
                    .user(user)

                    .cartItems(new ArrayList<>())
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .build();
            log.info("Tạo Cart thành công");
            return cartRepository.save(newCart);
        });

        for (ProductQuantityReq itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (itemReq.getQuantity() > product.getProductQuantity() || itemReq.getQuantity() < 0 || itemReq.getQuantity() > 10000) {
                throw new RuntimeException("Cannot add more quantity for product " + product.getProductName());
            }

            //? Check Product đã có trong Cart hay chưa
            //? Sẽ đi từ Cart -> CartItem -> Product -> ProductID
            Optional<CartItem> existingProductInCart = cart
                    .getCartItems()
                    .stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(itemReq.getProductId()))
                    .findFirst();

            CartItem cartItem;

            //?  Nếu Product tồn tại trong Cart rồi thì chỉ cần Update lại Quantity của Product đó thôi
            //? Nếu không thì build CartItem
            if (existingProductInCart.isPresent()) {
                cartItem = existingProductInCart.get();
                cartItem.setQuantity(cartItem.getQuantity() + itemReq.getQuantity());
                cartItem.setUpdateAt(LocalDateTime.now());
            } else {
                cartItem = CartItem
                        .builder()
                        .cart(cart)
                        .product(product)
                        .quantity(itemReq.getQuantity())
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .build();
                cart.getCartItems().add(cartItem);
            }
        }

            //? Tính tổng giá tiền theo product.price * product quantity
            //? sau đó dùng sum() để cộng lại toàn bộ mà thôi
             cart.setTotalPrice(
                cart.getCartItems().stream()
                        .map(item -> item.getProduct().getProductPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
             );

            Cart savedCart = cartRepository.save(cart);

            List<CartItemDetailRes> cartItemDetailResList = savedCart.getCartItems().stream()
                    .map(ci -> CartItemDetailRes.builder()
                            .cartItemId(ci.getCartItemId())
                            .cartId(savedCart.getCartId())
                            .product(ProductForCartItem.builder()
                                    .productId(ci.getProduct().getProductId())
                                    .productName(ci.getProduct().getProductName())
                                    .productImage(ci.getProduct().getProductImage())
                                    .productVolume(ci.getProduct().getProductVolume())
                                    .productUnit(ci.getProduct().getProductUnit())
                                    .productCode(ci.getProduct().getProductCode())
                                    .productQuantity(ci.getQuantity())
                                    .discount(ci.getProduct().getDiscount())
                                    .productPrice(ci.getProduct().getProductPrice())
                                    .colorName(ci.getProduct().getColors().getColorCode())
                                    .categoryName(ci.getProduct().getCategory().getCategoryName())
                                    .build())
                            .createAt(ci.getCreateAt())
                            .updateAt(ci.getUpdateAt())
                            .build())
                    .toList();

            return GetCartRes.builder()
                    .cartId(savedCart.getCartId())
                    .userId(userId)
                    .totalPrice(savedCart.getTotalPrice())
                    .createdAt(savedCart.getCreateAt())
                    .updatedAt(savedCart.getUpdateAt())
                    .items(cartItemDetailResList)
                    .build();
        }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_STAFF' , 'ROLE_ADMIN')")
    public GetCartRes getCart(String cartId) {
        return cartMapper.toGetCartRes(cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found")));
    }
}