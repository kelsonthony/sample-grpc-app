package com.kelsonthony.samplegrpcapp.api.controller;

import com.kelsonthony.samplegrpcapp.domain.model.User;
import com.kelsonthony.samplegrpcapp.domain.repository.UserRepository;
import com.kelsonthony.samplegrpcapp.v1.user.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class UserController extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(UserReq request, StreamObserver<UserRes> responseObserver) {
        User user = new User(request.getName(), request.getEmail());
        User saved = userRepository.save(user);

        UserRes userRes = UserRes.newBuilder()
                .setId(saved.getId())
                .setName(saved.getName())
                .setEmail(saved.getEmail())
                .build();

        responseObserver.onNext(userRes);
        responseObserver.onCompleted();

    }

    @Override
    public void getAll(EmptyReq request, StreamObserver<UserResList> responseObserver) {
        List<User> users = userRepository.findAll();

        List<UserRes> userRes = users.stream().map(user -> UserRes.newBuilder()
                        .setId(user.getId())
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .build())
                .toList();

        UserResList userResList = UserResList.newBuilder().addAllUsers(userRes).build();

        responseObserver.onNext(userResList);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllServerStream(EmptyReq request, StreamObserver<UserRes> responseObserver) {
        userRepository.findAll().forEach(user -> {
            UserRes userRes = UserRes.newBuilder()
                    .setId(user.getId())
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            responseObserver.onNext(userRes);
        });

        responseObserver.onCompleted();
    }
}
